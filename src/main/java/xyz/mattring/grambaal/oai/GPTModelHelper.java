package xyz.mattring.grambaal.oai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GPTModelHelper {

    final List<GPTModel> sortedModels;

    public GPTModelHelper(boolean excludeModelsWithoutApiKeys) {
        List<GPTModel> models = new ArrayList<>(List.of(GPTModel.values()));
        if (excludeModelsWithoutApiKeys) {
            models.removeIf(m -> !isApiKeyCurrentlySet(m.getModelName()));
        }
        models.sort((o1, o2) -> o1.getMaxTokens() - o2.getMaxTokens()); // by maxTokens ascending
        sortedModels = Collections.unmodifiableList(models);
    }

    public List<GPTModel> getSortedModels() {
        return sortedModels;
    }

    public Optional<GPTModel> getModelForMaxTokens(int maxTokens) {
        return sortedModels.stream()
                .filter(m -> m.getMaxTokens() >= maxTokens)
                .findFirst();
    }

    public Optional<GPTModel> getModelForModelName(String modelName) {
        return sortedModels.stream()
                .filter(m -> m.getModelName().equals(modelName))
                .findFirst();
    }

    public Optional<GPTModel> getModelForEnumName(String enumName) {
        return sortedModels.stream()
                .filter(m -> m.name().equals(enumName))
                .findFirst();
    }

    // TODO: add a unit test for getAPISpecForModelName()
    public static Optional<APISpec> getAPISpecForModelName(String modelName) {
        if (modelName == null) {
            return Optional.empty();
        } else if (modelName.startsWith("gpt-") || modelName.startsWith("o1-")) {
            return Optional.of(APISpec.GPT);
        } else if (modelName.startsWith("gemini-")) {
            return Optional.of(APISpec.GEMINI);
        } else if (modelName.startsWith("claude-")) {
            return Optional.of(APISpec.CLAUDE);
        } else if (modelName.contains("stral-")) {
            return Optional.of(APISpec.MISTRAL);
        } else if (modelName.startsWith("grok-")) {
            return Optional.of(APISpec.XAI);
        } else if (modelName.contains("/")) {
            return Optional.of(APISpec.DINFRA);
        } else {
            return Optional.empty();
        }
    }

    public static boolean isApiKeyCurrentlySet(String modelName) {
        Optional<APISpec> apiSpec = getAPISpecForModelName(modelName);
        return apiSpec.isPresent() && System.getenv(apiSpec.get().getApiKeyEnvVar()) != null;
    }
}
