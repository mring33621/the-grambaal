package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_1106_PREVIEW("gpt-4-1106-preview", 128000, 202304),
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview", 128000, 202304),
    GPT_4("gpt-4", 8192, 202109),
    GPT_4_32K("gpt-4-32k", 32768, 202109),
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106", 16385, 202109),
    GPT_3_5_TURBO("gpt-3.5-turbo", 4096, 202109),
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k", 16385, 202109);

    private final String modelName;
    private final int maxTokens;
    private final int trainingDataEndDate;

    GPTModel(String modelName, int maxTokens, int trainingDataEndDate) {
        this.modelName = modelName;
        this.maxTokens = maxTokens;
        this.trainingDataEndDate = trainingDataEndDate;
    }

    public String getModelName() {
        return modelName;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public int getTrainingDataEndDate() {
        return trainingDataEndDate;
    }

    @Override
    public String toString() {
        return String.format("%s, %d, %d", modelName, maxTokens, trainingDataEndDate);
    }
}