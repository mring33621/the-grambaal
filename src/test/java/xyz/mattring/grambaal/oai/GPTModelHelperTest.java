package xyz.mattring.grambaal.oai;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GPTModelHelperTest {

    static final GPTModelHelper GPT_MODEL_HELPER = new GPTModelHelper(false);

    @org.junit.jupiter.api.Test
    void modelEnumToString() {
        String observed = GPT_MODEL_HELPER.getSortedModels().stream()
                .map(GPTModel::toString)
                .collect(Collectors.joining("\n"));
//        System.out.println(observed);
        String expected =
                "deepseek-ai/DeepSeek-V3, 16000, 202401\n" +
                        "deepseek-ai/DeepSeek-R1, 16000, 202401\n" +
                        "Qwen/QwQ-32B-Preview, 32000, 202401\n" +
                        "Qwen/Qwen2.5-Coder-32B-Instruct, 32000, 202401\n" +
                        "mistral-small-latest, 32000, 202404\n" +
                        "NovaSky-AI/Sky-T1-32B-Preview, 32768, 202401\n" +
                        "gpt-4o-mini, 128000, 202310\n" +
                        "o1-mini, 128000, 202310\n" +
                        "nvidia/Llama-3.1-Nemotron-70B-Instruct, 128000, 202401\n" +
                        "deepseek-ai/DeepSeek-R1-Distill-Qwen-32B, 128000, 202401\n" +
                        "ministral-8b-latest, 128000, 202404\n" +
                        "deepseek-ai/DeepSeek-R1-Distill-Llama-70B, 131000, 202401\n" +
                        "mistral-large-latest, 131000, 202404\n" +
                        "grok-2-latest, 131072, 202406\n" +
                        "claude-3-5-haiku-20241022, 200000, 202407\n" +
                        "claude-3-5-sonnet-20241022, 200000, 202404\n" +
                        "codestral-latest, 256000, 202404\n" +
                        "gemini-2.0-flash-lite-preview-02-05, 1048576, 202411\n" +
                        "gemini-2.0-flash-001, 1048576, 202411\n" +
                        "gemini-2.0-flash-thinking-exp-01-21, 1048576, 202411\n" +
                        "gemini-exp-1206, 1048576, 202411\n" +
                        "gemini-2.0-pro-exp-02-05, 1048576, 202411";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(GPTModel.GPT_4o_mini, GPT_MODEL_HELPER.getModelForMaxTokens(128000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(GPTModel.DINFRA_DEEPSEEK_V3, GPT_MODEL_HELPER.getModelForModelName("deepseek-ai/DeepSeek-V3").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(GPTModel.CLAUDE_3_5_SONNET, GPT_MODEL_HELPER.getModelForEnumName("CLAUDE_3_5_SONNET").get());
    }
}