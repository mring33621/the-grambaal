package xyz.mattring.grambaal;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GPTSessionInteractorTest {

    @Test
    void expandTilde() {
        String result = GPTSessionInteractor.expandTildeAndNormalizePath("~/a");
        System.out.println(result);
        assertTrue(result.contains("home") || result.contains("Users"));
    }

    @Test
    void getExistingSessions() {
        Path testDirPath = null;
        try {
            testDirPath = Files.createTempDirectory("grambaal-unittest");
            testDirPath.toFile().deleteOnExit();
            Files.createFile(testDirPath.resolve("session-test1.txt"));
            Thread.sleep(1000); // give the OS a chance to do async file stuff
            Files.createFile(testDirPath.resolve("session-test2.txt"));
            List<String> result = GPTSessionInteractor.getExistingSessions(testDirPath.toString());
            System.out.println(result);
            assertEquals("[test2, test1]", result.toString()); // most recent first
        } catch (Exception e) {
            fail(e);
        }
    }
}