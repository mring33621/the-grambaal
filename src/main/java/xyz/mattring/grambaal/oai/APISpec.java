package xyz.mattring.grambaal.oai;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.function.UnaryOperator;

public enum APISpec {
    GPT(
            "GPT_API_KEY",
            "https://api.openai.com/v1/chat/completions",
            """
                    {
                        "model": "%s",
                        "max_tokens": 4096,
                        "messages": [
                            {
                                "role": "user",
                                "content": %s
                            }
                        ]
                    }
                    """,
            jsonStrResp -> {
                final JSONObject respObj = new JSONObject(jsonStrResp);
                final JSONArray choices = respObj.getJSONArray("choices");
                return choices.getJSONObject(0).getJSONObject("message").getString("content");
            }
    ),
    GEMINI(
            "GEM_API_KEY",
            // TODO: templatize the URL with the model name
            "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent",
            """
                    { "contents":[
                           { "parts":[{"text": %s}]}
                      ]
                    }
                    """,
            jsonStrResp -> {
                final JSONObject respObj = new JSONObject(jsonStrResp);
                final JSONArray choices = respObj.getJSONArray("candidates");
                return choices.getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text");
            }
    ),
    DINFRA(
            "DINFRA_API_KEY",
            "https://api.deepinfra.com/v1/openai/chat/completions",
            """
                    {
                        "model": "%s",
                        "max_tokens": 4096,
                        "messages": [
                            {
                                "role": "user",
                                "content": %s
                            }
                        ]
                    }
                    """,
            jsonStrResp -> {
                final JSONObject respObj = new JSONObject(jsonStrResp);
                final JSONArray choices = respObj.getJSONArray("choices");
                return choices.getJSONObject(0).getJSONObject("message").getString("content");
            }
    ),
    CLAUDE(
            "CLAUDE_API_KEY",
            "https://api.anthropic.com/v1/messages",
            """
                    {
                        "model": "%s",
                        "max_tokens": 4096,
                        "messages": [
                            {
                                "role": "user",
                                "content": %s
                            }
                        ]
                    }
                    """,
            jsonStrResp -> {
                final JSONObject respObj = new JSONObject(jsonStrResp);
                final JSONArray choices = respObj.getJSONArray("choices");
                return choices.getJSONObject(0).getJSONObject("message").getString("content");
            }
    );

    private final String apiKeyEnvVar;
    private final String apiUrl;
    private final String requestTemplate;
    private final UnaryOperator<String> jsonToChatResponseParser;

    APISpec(String apiKeyEnvVar, String apiUrl, String requestTemplate, UnaryOperator<String> jsonToChatResponseParser) {
        this.apiKeyEnvVar = apiKeyEnvVar;
        this.apiUrl = apiUrl;
        this.requestTemplate = requestTemplate;
        this.jsonToChatResponseParser = jsonToChatResponseParser;
    }

    public String getApiKeyEnvVar() {
        return apiKeyEnvVar;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public String getRequestTemplate() {
        return requestTemplate;
    }

    public UnaryOperator<String> getJsonToChatResponseParser() {
        return jsonToChatResponseParser;
    }
}
