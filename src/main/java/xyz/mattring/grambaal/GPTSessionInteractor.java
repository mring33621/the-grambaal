package xyz.mattring.grambaal;

import org.json.JSONObject;
import xyz.mattring.grambaal.oai.APISpec;
import xyz.mattring.grambaal.oai.GPTModel;
import xyz.mattring.grambaal.oai.GPTModelHelper;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import static xyz.mattring.grambaal.convo.ConvoUtils.*;

public class GPTSessionInteractor implements Runnable {

    public static final String SESSION_BASEDIR = "~/grambaal/sessions";

    static String expandTildeAndNormalizePath(String path) {
        String expandedPath = path.replaceFirst("^~", Matcher.quoteReplacement(System.getProperty("user.home")));
        return Path.of(expandedPath).normalize().toString();
    }

    static void appendContentAndDividers(File sessionFile, String content, String divider, String model) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(sessionFile, true))) {
            pw.println(annotateDivider(divider, model));
            pw.println(content);
            pw.println(getEndDivider(divider));
            pw.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static List<String> getExistingSessions(String sessionBaseDir) {
        String sessionPath = expandTildeAndNormalizePath(sessionBaseDir);
        File sessionDir = new File(sessionPath);
        File[] sessionFiles = sessionDir.listFiles();
        // Sort the files by last modified date in descending order
        Arrays.sort(sessionFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));
        return Stream.of(sessionFiles)
                .map(File::getName)
                .filter(f -> f.startsWith("session-"))
                .map(f -> f.replaceFirst("session-", ""))
                .map(f -> f.replaceFirst("\\.txt$", ""))
                .toList();
    }

    static HttpResponse<String> post(APISpec apiSpec, String apiKey, String modelName, String content) throws IOException {
        String apiVersion = "v1";
        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2);
        try (HttpClient httpClient = builder.build()) {
            String authHdrKey;
            String authHdrVal;
            Map<String, String> extraHeaders = new HashMap<>();
            // TODO: would be nice if the APISpec provided better parts, to avoid this if-then logic here
            if (apiSpec == APISpec.GPT || apiSpec == APISpec.DINFRA || apiSpec == APISpec.XAI) {
                // NOTE: DeepInfra and XAI provide an OpenAI API-compatible endpoint
                authHdrKey = "Authorization";
                authHdrVal = "Bearer " + apiKey;
            } else if (apiSpec == APISpec.GEMINI) {
                // experimental models seem to use alpha api version
                if (modelName.endsWith("-exp")) {
                    apiVersion = "v1alpha";
                }
                authHdrKey = "x-goog-api-key";
                authHdrVal = apiKey;
            } else if (apiSpec == APISpec.CLAUDE) {
                authHdrKey = "x-api-key";
                authHdrVal = apiKey;
                extraHeaders.put("anthropic-version", "2023-06-01");
            } else {
                throw new RuntimeException("Unknown API spec: " + apiSpec);
            }
            final String reqTemplate = apiSpec.getRequestTemplate();
            System.out.println("reqTemplate:\n" + reqTemplate);
            final String reqBody = reqTemplate
                    .replace("$modelName", modelName)
                    .replace("$content", JSONObject.quote(content));
            System.out.println("reqBody:\n" + reqBody);
            final String reqUrl = apiSpec.getApiUrl()
                    .replace("$apiVersion", apiVersion)
                    .replace("$modelName", modelName);
            System.out.println("reqUrl:\n" + reqUrl);
            HttpRequest.Builder reqBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(reqUrl))
                    .timeout(Duration.ofSeconds(180L))
                    .header("Content-Type", "application/json")
                    .header(authHdrKey, authHdrVal)
                    .POST(HttpRequest.BodyPublishers.ofString(reqBody));
            for (Map.Entry<String, String> stringStringEntry : extraHeaders.entrySet()) {
                reqBuilder = reqBuilder.header(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
            final HttpRequest request = reqBuilder.build();
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
        this(session, newUserPromptFile, GPTModel.GPT_o1_mini);
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

    public static void saveConvoTextForSession(String session, String convoText) {
        if (session == null) {
            return;
        }
        String sessionName = fixSessionFileName(session);
        String sessionPath = expandTildeAndNormalizePath(SESSION_BASEDIR);
        File sessionFile =
                Path.of(sessionPath, sessionName + ".txt").toFile();
        try (PrintWriter pw = new PrintWriter(new FileWriter(sessionFile, false))) {
            pw.println(convoText);
            pw.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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
            throw new UncheckedIOException(e);
        }

        String fullSessionConvo = null;
        try {
            fullSessionConvo = Files.readString(sessionFile.toPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
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
            throw new UncheckedIOException(e);
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