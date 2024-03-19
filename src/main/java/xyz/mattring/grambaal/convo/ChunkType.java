package xyz.mattring.grambaal.convo;

import java.util.Optional;

enum ChunkType {
    SystemPrompt("system_prompt"),
    OriginalUserPrompt("original_user_prompt"),
    AssistantResponse("assistant_response"),
    UserFollowup("user_followup");

    private final String tagName;

    ChunkType(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public String getAsDividerTag() {
        return "<" + tagName + ">";
    }

    public static Optional<ChunkType> fromTag(String tag) {
        ChunkType selectedType = null;
        for (ChunkType type : ChunkType.values()) {
            if (tag.contains(type.getTagName())) {
                selectedType = type;
                break;
            }
        }
        return Optional.ofNullable(selectedType);
    }
}
