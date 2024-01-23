package xyz.mattring.grambaal.oai;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OAIModelHelperTest {

    static final OAIModelHelper OAI_MODEL_HELPER = new OAIModelHelper();

    @org.junit.jupiter.api.Test
    void modelEnumToString() {
        String observed = OAI_MODEL_HELPER.getSortedModels().stream()
                .map(OAIModelHelper::modelEnumToString)
                .collect(Collectors.joining("\n"));
        final String expected = "gpt-3.5-turbo, 4096\n" +
                "gpt-4, 8192\n" +
                "gpt-3.5-turbo-1106, 16385\n" +
                "gpt-3.5-turbo-16k, 16385\n" +
                "gpt-4-32k, 32768\n" +
                "gpt-4-1106-preview, 128000";
        assertEquals(expected, observed);
    }

    @org.junit.jupiter.api.Test
    void getModelForTokens() {
        assertEquals(OAIModel.gpt_4, OAI_MODEL_HELPER.getModelForTokens(5000).get());
    }

    @org.junit.jupiter.api.Test
    void getModelForModelName() {
        assertEquals(OAIModel.gpt_3_5_turbo_1106, OAI_MODEL_HELPER.getModelForModelName("gpt-3.5-turbo-1106").get());
    }

    @org.junit.jupiter.api.Test
    void getModelForEnumName() {
        assertEquals(OAIModel.gpt_3_5_turbo_16k, OAI_MODEL_HELPER.getModelForEnumName("gpt_3_5_turbo_16k").get());
    }
}