package xyz.mattring.grambaal.oai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OAIModelHelper {

    public static String modelEnumToString(OAIModel model) {
        return String.format("%s, %d", model.getModelName(), model.getMaxTokens());
    }

    final List<OAIModel> sortedModels;

    public OAIModelHelper() {
        List<OAIModel> models = new ArrayList<>(List.of(OAIModel.values()));
        models.sort((o1, o2) -> o1.getMaxTokens() - o2.getMaxTokens()); // by maxTokens ascending
        sortedModels = Collections.unmodifiableList(models);
    }

    public List<OAIModel> getSortedModels() {
        return Collections.unmodifiableList(sortedModels);
    }

    public Optional<OAIModel> getModelForTokens(int tokens) {
        return sortedModels.stream()
                .filter(m -> m.getMaxTokens() >= tokens)
                .findFirst();
    }

    public Optional<OAIModel> getModelForModelName(String modelName) {
        return sortedModels.stream()
                .filter(m -> m.getModelName().equals(modelName))
                .findFirst();
    }

    public Optional<OAIModel> getModelForEnumName(String enumName) {
        return sortedModels.stream()
                .filter(m -> m.name().equals(enumName))
                .findFirst();
    }
}
