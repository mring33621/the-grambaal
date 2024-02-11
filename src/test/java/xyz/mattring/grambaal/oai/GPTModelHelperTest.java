package xyz.mattring.grambaal.oai;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GPTModelHelperTest {

    static final GPTModelHelper OAI_MODEL_HELPER = new GPTModelHelper();

    @org.junit.jupiter.api.Test
    void modelEnumToString() {
        String observed = OAI_MODEL_HELPER.getSortedModels().stream()
                .map(GPTModel::toString)
                .collect(Collectors.joining("\n"));
        String expected = "01-ai/Yi-34B-Chat, 4000, 202307\n" +
                "gpt-3.5-turbo, 4096, 202109\n" +
                "gpt-3.5-turbo-instruct, 4096, 202109\n" +
                "gpt-4, 8192, 202109\n" +
                "gpt-4-0613, 8192, 202109\n" +
                "gpt-3.5-turbo-0125, 16385, 202109\n" +
                "gpt-3.5-turbo-1106, 16385, 202109\n" +
                "gpt-4-32k, 32768, 202109\n" +
                "gpt-4-32k-0613, 32768, 202109\n" +
                "cognitivecomputations/dolphin-2.6-mixtral-8x7b, 32768, 202307\n" +
                "codellama/CodeLlama-34b-Instruct-hf, 100000, 202307\n" +
                "gpt-4-0125-preview, 128000, 202304\n" +
                "gpt-4-turbo-preview, 128000, 202304\n" +
                "gpt-4-1106-preview, 128000, 202304\n" +
                "gpt-4-vision-preview, 128000, 202304";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(GPTModel.GPT_4, OAI_MODEL_HELPER.getModelForMaxTokens(5000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(GPTModel.GPT_3_5_TURBO_1106, OAI_MODEL_HELPER.getModelForModelName("gpt-3.5-turbo-1106").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(GPTModel.GPT_3_5_TURBO_INSTRUCT, OAI_MODEL_HELPER.getModelForEnumName("GPT_3_5_TURBO_INSTRUCT").get());
    }
}