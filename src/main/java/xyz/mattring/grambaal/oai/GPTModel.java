package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview", 128000, 202304),
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview", 128000, 202304),
    GPT_4("gpt-4", 8192, 202109),
    GPT_3_5_TURBO("gpt-3.5-turbo", 4096, 202109),
    GEM_PRO("gemini-pro", 30720, 202402),
    GEM_1_0_PRO_LATEST("gemini-1.0-pro-latest", 30720, 202402),
    DINFRA_DOLPHIN_2_6("cognitivecomputations/dolphin-2.6-mixtral-8x7b", 32768, 202307),
    DINFRA_YI_34B("01-ai/Yi-34B-Chat", 4000, 202307),
    DINFRA_CODE_LLAMA_34B("codellama/CodeLlama-34b-Instruct-hf", 100000, 202307);

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
