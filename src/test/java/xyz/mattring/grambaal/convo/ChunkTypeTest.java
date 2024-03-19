package xyz.mattring.grambaal.convo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ChunkTypeTest {

    @Test
    void fromTag() {
        assertEquals(ChunkType.SystemPrompt, ChunkType.fromTag("<system_prompt>").get());
        assertEquals(ChunkType.OriginalUserPrompt, ChunkType.fromTag("<original_user_prompt>").get());
        assertEquals(ChunkType.AssistantResponse, ChunkType.fromTag("<assistant_response>").get());
        assertEquals(ChunkType.UserFollowup, ChunkType.fromTag("<user_followup>").get());
        assertFalse(ChunkType.fromTag("<some_tag>").isPresent());
    }
}