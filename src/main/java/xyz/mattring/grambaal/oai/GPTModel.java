package xyz.mattring.grambaal.oai;

public enum GPTModel {
    GPT_4_0125_PREVIEW("gpt-4-0125-preview", 128000, 202304),
    GPT_4_TURBO_PREVIEW("gpt-4-turbo-preview", 128000, 202304),
    GPT_4_1106_PREVIEW("gpt-4-1106-preview", 128000, 202304),
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview", 128000, 202304),
    GPT_4("gpt-4", 8192, 202109),
    GPT_4_0613("gpt-4-0613", 8192, 202109),
    GPT_4_32K("gpt-4-32k", 32768, 202109),
    GPT_4_32K_0613("gpt-4-32k-0613", 32768, 202109),
    GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125", 16385, 202109),
    GPT_3_5_TURBO("gpt-3.5-turbo", 4096, 202109), // Note: This entry will be updated to point to gpt-3.5-turbo-0125 on February 16th.
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106", 16385, 202109),
    GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct", 4096, 202109),
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
