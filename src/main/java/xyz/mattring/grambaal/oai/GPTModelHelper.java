package xyz.mattring.grambaal.oai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class GPTModelHelper {

    final List<GPTModel> sortedModels;

    public GPTModelHelper() {
        List<GPTModel> models = new ArrayList<>(List.of(GPTModel.values()));
        models.sort((o1, o2) -> o1.getMaxTokens() - o2.getMaxTokens()); // by maxTokens ascending
        sortedModels = Collections.unmodifiableList(models);
    }

    public List<GPTModel> getSortedModels() {
        return Collections.unmodifiableList(sortedModels);
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

    public static Optional<APISpec> getAPISpecForModelName(String modelName) {
        if (modelName == null) {
            return Optional.empty();
        } else if (modelName.startsWith("gpt-")) {
            return Optional.of(APISpec.GPT);
        } else if (modelName.startsWith("gemini-")) {
            return Optional.of(APISpec.GEMINI);
        }  else if (modelName.contains("/")) {
            return Optional.of(APISpec.DINFRA);
        } else {
            return Optional.empty();
        }
    }
}
