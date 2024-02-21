package xyz.mattring.grambaal.oai;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GPTModelHelperTest {

    static final GPTModelHelper GPT_MODEL_HELPER = new GPTModelHelper();

    @org.junit.jupiter.api.Test
    void modelEnumToString() {
        String observed = GPT_MODEL_HELPER.getSortedModels().stream()
                .map(GPTModel::toString)
                .collect(Collectors.joining("\n"));
        String expected = "01-ai/Yi-34B-Chat, 4000, 202307\n" +
                "gpt-3.5-turbo, 4096, 202109\n" +
                "gpt-4, 8192, 202109\n" +
                "gemini-pro, 30720, 202402\n" +
                "gemini-1.0-pro-latest, 30720, 202402\n" +
                "cognitivecomputations/dolphin-2.6-mixtral-8x7b, 32768, 202307\n" +
                "codellama/CodeLlama-34b-Instruct-hf, 100000, 202307\n" +
                "gpt-4-turbo-preview, 128000, 202304\n" +
                "gpt-4-vision-preview, 128000, 202304";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(GPTModel.GPT_4, GPT_MODEL_HELPER.getModelForMaxTokens(5000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(GPTModel.GPT_4_TURBO_PREVIEW, GPT_MODEL_HELPER.getModelForModelName("gpt-4-turbo-preview").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(GPTModel.GEM_1_0_PRO_LATEST, GPT_MODEL_HELPER.getModelForEnumName("GEM_1_0_PRO_LATEST").get());
    }
}