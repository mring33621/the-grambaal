package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4o("chatgpt-4o-latest", 128000, 202310),
    GPT_o1("o1-preview", 128000, 202310),
    GPT_o1_mini("o1-mini", 128000, 202310),
    GEM_1_5_PRO_LATEST("gemini-1.5-pro", 1048576, 202409),
    GEM_1_5_FLASH_LATEST("gemini-1.5-flash", 1048576, 202409),
    GEM_2_0_FLASH_LATEST("gemini-2.0-flash-exp", 1048576, 202411),
    GEM_2_0_FLASH_THINK_LATEST("gemini-2.0-flash-thinking-exp-01-21", 1048576, 202411),
    GEM_EXP("gemini-exp-1206", 1048576, 202411),
    DINFRA_QWEN_QwQ_32B("Qwen/QwQ-32B-Preview", 32000, 202401),
    DINFRA_QWEN_2_5_CODER("Qwen/Qwen2.5-Coder-32B-Instruct", 32000, 202401),
    DINFRA_SKY_T1("NovaSky-AI/Sky-T1-32B-Preview", 32768, 202401),
    DINFRA_LLAMA_3_1_TURBO_70B("meta-llama/Meta-Llama-3.1-70B-Instruct-Turbo", 32000, 202401),
    DINFRA_LLAMA_3_3_TURBO_70B("meta-llama/Llama-3.3-70B-Instruct-Turbo", 32000, 202401),
    DINFRA_LLAMA_3_1_405B("meta-llama/Meta-Llama-3.1-405B-Instruct", 32000, 202401),
    DINFRA_NEMO("nvidia/Llama-3.1-Nemotron-70B-Instruct", 128000, 202401),
    DINFRA_DEEPSEEK_V3("deepseek-ai/DeepSeek-V3", 16000, 202401),
    DINFRA_DEEPSEEK_R1("deepseek-ai/DeepSeek-R1", 16000, 202401),
    DINFRA_DS_LLAMA_70B("deepseek-ai/DeepSeek-R1-Distill-Llama-70B", 131000, 202401),
    CLAUDE_3_5_HAIKU("claude-3-5-haiku-20241022", 200000, 202407),
    CLAUDE_3_5_SONNET("claude-3-5-sonnet-20241022", 200000, 202404),
    MISTRAL_MINI_8B("ministral-8b-latest", 128000, 202404),
    MISTRAL_SMALL("mistral-small-latest", 32000, 202404),
    MISTRAL_LARGE("mistral-large-latest", 131000, 202404),
    MISTRAL_CODESTRAL("codestral-latest", 256000, 202404),
    XAI_GROK_2_LATEST("grok-2-latest",131072, 202406);

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
