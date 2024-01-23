package xyz.mattring.grambaal.oai;

public enum OAIModel {
    gpt_4_1106_preview(128_000, 202304),
    gpt_4_32k(32_768, 202109),
    gpt_4(8_192, 202109),
    gpt_3_5_turbo_1106(16_385, 202109),
    gpt_3_5_turbo_16k(16_385, 202109),
    gpt_3_5_turbo(4_096, 202109);

    private final int maxTokens;
    private final int trainingDataEndDateYYYYMM;
    private final String modelName;

    OAIModel(int maxTokens, int trainingDataEndDateYYYYMM) {
        this.maxTokens = maxTokens;
        this.trainingDataEndDateYYYYMM = trainingDataEndDateYYYYMM;
        this.modelName = name().replace('_', '-').replace("3-5", "3.5");
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public int getTrainingDataEndDateYYYYMM() {
        return trainingDataEndDateYYYYMM;
    }

    public String getModelName() {
        return modelName;
    }
}
