package xyz.mattring.grambaal;

import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.regex.Matcher;

public class GPTSessionInteractor implements Runnable {

    static final String API_URL = "https://api.openai.com/v1/chat/completions";
    static final String ORIG_PROMPT_DIVIDER = "<original_user_prompt>";
    static final String GPT_RESP_DIVIDER = "<assistant_response>";
    static final String USER_FOLLOWUP_DIVIDER = "<user_followup>";
    public static final String GRAMBAAL_API_KEY = "GRAMBAAL_API_KEY";

    static String expandTilde(String path) {
        return path.replaceFirst("^~", Matcher.quoteReplacement(System.getProperty("user.home")));
    }

    static String getEndDivider(String divider) {
        return divider.replace("<", "</");
    }

    static void appendContentAndDividers(File sessionFile, String content, String divider) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(sessionFile, true))) {
            pw.println(divider);
            pw.println(content);
            pw.println(getEndDivider(divider));
            pw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static HttpResponse<String> post(String url, String apiKey, String content) throws IOException {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2);
        try (HttpClient httpClient = builder.build()) {
            final String reqTemplate = """
                    {
                          "model": "gpt-3.5-turbo",
                          "messages": [{"role": "user", "content": %s}],
                          "temperature": 0.7
                    }
                    """;
            final String reqBody = String.format(reqTemplate, JSONObject.quote(content));
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .timeout(Duration.ofMinutes(1))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
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

    public static String extractAssistantResponse(String jsonResponse) {
        JSONObject response = new JSONObject(jsonResponse);
        JSONArray choices = response.getJSONArray("choices");
        return choices.getJSONObject(0).getJSONObject("message").getString("content");
    }

    private final String session;
    private final String newUserPromptFile;

    public GPTSessionInteractor(String session, String newUserPromptFile) {
        this.session = session;
        this.newUserPromptFile = newUserPromptFile;
    }

    @Override
    public void run() {
        String sessionPath = expandTilde("~/grambaal/sessions");
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
            newUserPrompt = Files.readString(Path.of(expandTilde(newUserPromptFile)));
            appendContentAndDividers(sessionFile, newUserPrompt, divider);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fullSessionConvo = null;
        try {
            fullSessionConvo = Files.readString(sessionFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String apiKey = System.getenv(GRAMBAAL_API_KEY);
        if (apiKey == null) {
            throw new RuntimeException("GRAMBAAL_API_KEY environment variable not set");
        }

        String response = null;
        try {
            response = post(API_URL, apiKey, fullSessionConvo).body();
            boolean apiError = hasError(response);
            if (apiError) {
                System.err.println("API error: \n" + response);
                System.exit(1);
            }
            String assistantResponseTxt = extractAssistantResponse(response);
            appendContentAndDividers(sessionFile, assistantResponseTxt, GPT_RESP_DIVIDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    public static void main(String[] args) {
        String session = "session-" + args[0];
        String newUserPromptFile = args[1];
        GPTSessionInteractor GPTSessionInteractor = new GPTSessionInteractor(session, newUserPromptFile);
        GPTSessionInteractor.run();
    }

}