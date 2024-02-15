package xyz.mattring.grambaal;

import org.json.JSONObject;
import xyz.mattring.grambaal.oai.APISpec;
import xyz.mattring.grambaal.oai.GPTModel;
import xyz.mattring.grambaal.oai.GPTModelHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;

public class GPTSessionInteractor implements Runnable {

    static final String ORIG_PROMPT_DIVIDER = "<original_user_prompt>";
    static final String GPT_RESP_DIVIDER = "<assistant_response>";
    static final String USER_FOLLOWUP_DIVIDER = "<user_followup>";
    public static final String SESSION_BASEDIR = "~/grambaal/sessions";

    static String annotateDivider(String divider, String modelName) {
        String annotatedDivider = divider;
        if (GPT_RESP_DIVIDER.equals(divider)) {
            String modelAnno = String.format(" model=\"%s\"", modelName);
            annotatedDivider = annotatedDivider.replace(">", modelAnno + ">");
        }
        final LocalDateTime now = LocalDateTime.now();
        // Format the LocalDateTime object using the ISO 8601 date format
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        final String formattedDateTime = formatter.format(now);
        annotatedDivider = annotatedDivider.replace(">", " timestamp=\"" + formattedDateTime + "\">");
        return annotatedDivider;
    }

    static String expandTildeAndNormalizePath(String path) {
        String expandedPath = path.replaceFirst("^~", Matcher.quoteReplacement(System.getProperty("user.home")));
        String normalizedPath = Path.of(expandedPath).normalize().toString();
        return normalizedPath;
    }

    static String getEndDivider(String divider) {
        return divider.replace("<", "</");
    }

    static void appendContentAndDividers(File sessionFile, String content, String divider, String model) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(sessionFile, true))) {
            pw.println(annotateDivider(divider, model));
            pw.println(content);
            pw.println(getEndDivider(divider));
            pw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getExistingSessions(String sessionBaseDir) {
        String sessionPath = expandTildeAndNormalizePath(sessionBaseDir);
        File sessionDir = new File(sessionPath);
        String[] sessionFiles = sessionDir.list();
        return List.of(sessionFiles).stream()
                .filter(f -> f.startsWith("session-"))
                .map(f -> f.replaceFirst("session-", ""))
                .map(f -> f.replaceFirst("\\.txt$", ""))
                .toList();
    }

    static HttpResponse<String> post(APISpec apiSpec, String apiKey, String modelName, String content) throws IOException {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2);
        try (HttpClient httpClient = builder.build()) {
            final String reqTemplate = apiSpec.getRequestTemplate();
            String reqBody;
            String authHdrKey;
            String authHdrVal;
            // TODO: would be nice if the API spec provided better parts, to avoid this if-then logic here
            if (apiSpec == APISpec.GPT || apiSpec == APISpec.DINFRA) {
                // NOTE: DeepInfra provides an OpenAI API-compatible endpoint
                reqBody = String.format(reqTemplate, modelName, JSONObject.quote(content));
                authHdrKey = "Authorization";
                authHdrVal = "Bearer " + apiKey;
            } else if (apiSpec == APISpec.GEMINI) {
                reqBody = String.format(reqTemplate, JSONObject.quote(content));
                authHdrKey = "x-goog-api-key";
                authHdrVal = apiKey;
            } else {
                throw new RuntimeException("Unknown API spec: " + apiSpec);
            }
            System.out.println("Request body:\n" + reqBody);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiSpec.getApiUrl()))
                    .timeout(Duration.ofMinutes(2))
                    .header("Content-Type", "application/json")
                    .header(authHdrKey, authHdrVal)
                    .POST(HttpRequest.BodyPublishers.ofString(reqBody))
                    .build();
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean hasError(String jsonResponse) {
        JSONObject response = new JSONObject(jsonResponse);
        return response.has("error");
    }

    public static String extractAssistantResponse(String jsonResponse, APISpec apiSpec) {
        return apiSpec.getJsonToChatResponseParser().apply(jsonResponse);
    }

    static String fixSessionFileName(String sessionName) {
        if (sessionName == null) {
            return null;
        }
        if (!sessionName.startsWith("session-")) {
            sessionName = "session-" + sessionName;
        }
        return sessionName;
    }

    private final String session;
    private final String newUserPromptFile;
    private final String modelName;

    public GPTSessionInteractor(String session, String newUserPromptFile) {
        this(session, newUserPromptFile, GPTModel.GPT_4);
    }

    public GPTSessionInteractor(String session, String newUserPromptFile, String modelName) {
        this.session = fixSessionFileName(session);
        this.newUserPromptFile = newUserPromptFile;
        this.modelName = modelName;
    }

    public GPTSessionInteractor(String session, String newUserPromptFile, GPTModel model) {
        this(session, newUserPromptFile, model.getModelName());
    }

    public static Optional<String> getConvoTextForSession(String session) {
        if (session == null) {
            return Optional.empty();
        }
        String sessionName = fixSessionFileName(session);
        String sessionPath = expandTildeAndNormalizePath(SESSION_BASEDIR);
        File sessionFile =
                Path.of(sessionPath, sessionName + ".txt").toFile();
        String fullSessionConvo = null;
        try {
            fullSessionConvo = Files.readString(sessionFile.toPath());
        } catch (IOException e) {
            fullSessionConvo = "Error: " + e.getMessage();
        }
        return Optional.ofNullable(fullSessionConvo);
    }

    @Override
    public void run() {
        String sessionPath = expandTildeAndNormalizePath(SESSION_BASEDIR);
        File sessionFile =
                Path.of(sessionPath, session + ".txt").toFile();
        boolean existingSession = sessionFile.exists();
        String divider = existingSession ? USER_FOLLOWUP_DIVIDER : ORIG_PROMPT_DIVIDER;
        if (existingSession) {
            System.out.println("Session already exists: " + session);
        } else {
            System.out.println("Creating session: " + session);
            // creat session directories if they don't exist
            sessionFile.getParentFile().mkdirs();
        }

        // read new user prompt from newUserPromptFile
        String newUserPrompt = null;
        try {
            newUserPrompt = Files.readString(Path.of(expandTildeAndNormalizePath(newUserPromptFile)));
            appendContentAndDividers(sessionFile, newUserPrompt, divider, modelName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fullSessionConvo = null;
        try {
            fullSessionConvo = Files.readString(sessionFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final APISpec apiSpec = GPTModelHelper.getAPISpecForModelName(modelName).orElseThrow();

        final String envVar = apiSpec.getApiKeyEnvVar();
        final String apiKey = System.getenv(envVar);
        if (apiKey == null) {
            throw new RuntimeException(envVar + " environment variable not set");
        }

        String response = null;
        try {
            response = post(apiSpec, apiKey, modelName, fullSessionConvo).body();
            boolean apiError = hasError(response);
            if (apiError) {
                System.err.println("API error: \n" + response);
//                System.exit(1);
            }
            String assistantResponseTxt = extractAssistantResponse(response, apiSpec);
            appendContentAndDividers(sessionFile, assistantResponseTxt, GPT_RESP_DIVIDER, modelName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String session = "session-" + args[0];
        String modelName = args[1];
        String newUserPromptFile = args[2];
        GPTSessionInteractor GPTSessionInteractor = new GPTSessionInteractor(session, newUserPromptFile, modelName);
        GPTSessionInteractor.run();
//        System.exit(0);
    }

}