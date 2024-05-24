package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_TURBO("gpt-4-turbo", 128000, 202312),
    GPT_4o("gpt-4o", 128000, 202310),
    GPT_3_5_TURBO("gpt-3.5-turbo", 16385, 202109),
    GEM_1_5_PRO_LATEST("gemini-1.5-pro-latest", 1048576, 202303),
    GEM_1_5_FLASH_LATEST("gemini-1.5-flash-latest", 1048576, 202303),
    GEM_1_0_PRO_LATEST("gemini-1.0-pro-latest", 30720, 202303),
    DINFRA_DOLPHIN_2_6("cognitivecomputations/dolphin-2.6-mixtral-8x7b", 32000, 202307),
    DINFRA_LLAMA_3_8B("meta-llama/Meta-Llama-3-8B-Instruct", 8000, 202303),
    CLAUDE_3_HAIKU("claude-3-haiku-20240307", 200000, 202308),
    CLAUDE_3_SONNET("claude-3-sonnet-20240229", 200000, 202308);

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
