package xyz.mattring.grambaal.convo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConvoUtilsTest {

    @Test
    void annotateDivider() {
        String result = ConvoUtils.annotateDivider("<some_tag>", "gpt-6-platinum");
        System.out.println(result);
        assertTrue(result.contains("timestamp="));

        result = ConvoUtils.annotateDivider("<assistant_response>", "gpt-6-platinum");
        System.out.println(result);
        assertTrue(result.contains("timestamp="));
        assertTrue(result.contains("model=\"gpt-6-platinum\""));
    }

    @Test
    void getEndDivider() {
        assertEquals("</some_tag>", ConvoUtils.getEndDivider("<some_tag>"));
        assertEquals("</original_user_prompt>", ConvoUtils.getEndDivider(ConvoUtils.ORIG_PROMPT_DIVIDER));
    }

    @Test
    void splitAndJoinConvo() throws IOException {
        String convo = Files.readString(Paths.get("src/test/resources/session-example1.txt"));
        List<Chunk> result = ConvoUtils.splitConvo(convo);
        String rejoined = ConvoUtils.joinConvo(result);
        assertEquals(convo, rejoined);
    }
}