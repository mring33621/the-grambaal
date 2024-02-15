package xyz.mattring.grambaal;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GPTSessionInteractorTest {

    @Test
    void annotateDivider() {
        String result = GPTSessionInteractor.annotateDivider("<fart>", "gpt-6-platinum");
        System.out.println(result);
        assertTrue(result.contains("timestamp="));

        result = GPTSessionInteractor.annotateDivider("<assistant_response>", "gpt-6-platinum");
        System.out.println(result);
        assertTrue(result.contains("timestamp="));
        assertTrue(result.contains("model=\"gpt-6-platinum\""));
    }

    @Test
    void expandTilde() {
        String result = GPTSessionInteractor.expandTildeAndNormalizePath("~/a");
        System.out.println(result);
        assertTrue(result.contains("home") || result.contains("Users"));
    }

    @Test
    void getEndDivider() {
        assertEquals("</fart>", GPTSessionInteractor.getEndDivider("<fart>"));
        assertEquals("</original_user_prompt>", GPTSessionInteractor.getEndDivider(GPTSessionInteractor.ORIG_PROMPT_DIVIDER));
    }

    @Test
    void getExistingSessions() {
        Path testDirPath = null;
        try {
            testDirPath = Files.createTempDirectory("grambaal-unittest");
            testDirPath.toFile().deleteOnExit();
            Files.createFile(testDirPath.resolve("session-test1.txt"));
            Files.createFile(testDirPath.resolve("session-test2.txt"));
            List<String> result = GPTSessionInteractor.getExistingSessions(testDirPath.toString());
            System.out.println(result);
            assertEquals("[test1, test2]", result.toString());
        } catch (Exception e) {
            fail(e);
        }
    }
}