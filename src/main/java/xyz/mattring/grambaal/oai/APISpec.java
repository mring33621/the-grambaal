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
                        "model": "$modelName",
                        "max_completion_tokens": 8192,
                        "messages": [
                            {
                                "role": "user",
                                "content": $content
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
            "https://generativelanguage.googleapis.com/$apiVersion/models/$modelName:generateContent",
            """
                    { "contents":[
                           { "parts":[{"text": $content}]}
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
                        "model": "$modelName",
                        "max_tokens": 8192,
                        "messages": [
                            {
                                "role": "user",
                                "content": $content
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
                        "model": "$modelName",
                        "max_tokens": 8192,
                        "messages": [
                            {
                                "role": "user",
                                "content": $content
                            }
                        ]
                    }
                    """,
            jsonStrResp -> {
                final JSONObject respObj = new JSONObject(jsonStrResp);
                final JSONArray choices = respObj.getJSONArray("content");
                return choices.getJSONObject(0).getString("text");
            }
    ),
    MISTRAL(
            "MISTRAL_API_KEY",
            "https://api.mistral.ai/v1/chat/completions",
            """
                    {
                        "model": "$modelName",
                        "max_tokens": 8192,
                        "messages": [
                            {
                                "role": "user",
                                "content": $content
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
    XAI(
            "XAI_API_KEY",
            "https://api.x.ai/v1/chat/completions",
            """
                    {
                        "model": "$modelName",
                        "max_tokens": 8192,
                        "messages": [
                            {
                                "role": "user",
                                "content": $content
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
