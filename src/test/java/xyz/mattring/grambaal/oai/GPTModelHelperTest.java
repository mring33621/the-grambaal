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
                "Qwen/Qwen2.5-Coder-7B, 32000, 202401\n" +
                        "meta-llama/Meta-Llama-3.1-405B-Instruct, 32000, 202401\n" +
                        "gpt-4-turbo, 128000, 202312\n" +
                        "o1-preview, 128000, 202310\n" +
                        "o1-mini, 128000, 202310\n" +
                        "mistralai/Mistral-Nemo-Instruct-2407, 128000, 202401\n" +
                        "claude-3-haiku-20240307, 200000, 202308\n" +
                        "claude-3-opus-20240229, 200000, 202308\n" +
                        "claude-3-5-sonnet-20240620, 200000, 202404\n" +
                        "gemini-1.5-pro, 1048576, 202303\n" +
                        "gemini-1.5-flash, 1048576, 202303";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(GPTModel.GPT_4_TURBO, GPT_MODEL_HELPER.getModelForMaxTokens(128000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(GPTModel.GEM_1_5_FLASH_LATEST, GPT_MODEL_HELPER.getModelForModelName("gemini-1.5-flash").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(GPTModel.CLAUDE_3_5_SONNET, GPT_MODEL_HELPER.getModelForEnumName("CLAUDE_3_5_SONNET").get());
    }
}