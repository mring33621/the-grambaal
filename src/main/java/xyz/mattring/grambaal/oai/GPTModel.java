package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_TURBO("gpt-4-turbo", 128000, 202312),
    GPT_o1("o1-preview", 128000, 202310),
    GPT_o1_mini("o1-mini", 128000, 202310),
    GEM_1_5_PRO_LATEST("gemini-1.5-pro", 1048576, 202303),
    GEM_1_5_FLASH_LATEST("gemini-1.5-flash", 1048576, 202303),
    DINFRA_QWEN_2_5_CODER("Qwen/Qwen2.5-Coder-7B", 32000, 202401),
    DINFRA_LLAMA_3_1_405B("meta-llama/Meta-Llama-3.1-405B-Instruct", 32000, 202401),
    DINFRA_NEMO("mistralai/Mistral-Nemo-Instruct-2407", 128000, 202401),
    CLAUDE_3_HAIKU("claude-3-haiku-20240307", 200000, 202308),
    CLAUDE_3_OPUS("claude-3-opus-20240229", 200000, 202308),
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
