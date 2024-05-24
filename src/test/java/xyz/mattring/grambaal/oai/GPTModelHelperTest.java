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
        String expected = "meta-llama/Meta-Llama-3-8B-Instruct, 8000, 202303\n" +
                "gpt-3.5-turbo, 16385, 202109\n" +
                "gemini-1.0-pro-latest, 30720, 202303\n" +
                "cognitivecomputations/dolphin-2.6-mixtral-8x7b, 32000, 202307\n" +
                "gpt-4-turbo, 128000, 202312\n" +
                "gpt-4o, 128000, 202310\n" +
                "claude-3-haiku-20240307, 200000, 202308\n" +
                "claude-3-sonnet-20240229, 200000, 202308\n" +
                "gemini-1.5-pro-latest, 1048576, 202303\n" +
                "gemini-1.5-flash-latest, 1048576, 202303";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(GPTModel.GPT_3_5_TURBO, GPT_MODEL_HELPER.getModelForMaxTokens(16000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(GPTModel.GEM_1_5_FLASH_LATEST, GPT_MODEL_HELPER.getModelForModelName("gemini-1.5-flash-latest").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(GPTModel.DINFRA_LLAMA_3_8B, GPT_MODEL_HELPER.getModelForEnumName("DINFRA_LLAMA_3_8B").get());
    }
}