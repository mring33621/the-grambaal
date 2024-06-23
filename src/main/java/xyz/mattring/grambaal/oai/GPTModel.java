package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_TURBO("gpt-4-turbo", 128000, 202312),
    GPT_4o("gpt-4o", 128000, 202310),
    GPT_3_5_TURBO("gpt-3.5-turbo", 16385, 202109),
    GEM_1_5_PRO_LATEST("gemini-1.5-pro-latest", 1048576, 202303),
    GEM_1_5_FLASH_LATEST("gemini-1.5-flash-latest", 1048576, 202303),
    GEM_1_0_PRO_LATEST("gemini-1.0-pro-latest", 30720, 202303),
    DINFRA_QWEN_2_72B("Qwen/Qwen2-72B-Instruct", 32000, 202307),
    DINFRA_LLAMA_3_70B("meta-llama/Meta-Llama-3-70B-Instruct", 8000, 202303),
    DINFRA_NEMOTRON_4_340B("nvidia/Nemotron-4-340B-Instruct", 4000, 202306),
    CLAUDE_3_HAIKU("claude-3-haiku-20240307", 200000, 202308),
    CLAUDE_3_SONNET("claude-3-sonnet-20240229", 200000, 202308),
    CLAUDE_3_5_SONNET("claude-3-5-sonnet-20240620", 200000, 202404);

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
