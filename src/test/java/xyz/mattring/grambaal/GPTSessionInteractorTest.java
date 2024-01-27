package xyz.mattring.grambaal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        String result = GPTSessionInteractor.expandTilde("~/a");
        System.out.println(result);
        assertTrue(result.contains("home") || result.contains("Users"));
    }

    @Test
    void getEndDivider() {
        assertEquals("</fart>", GPTSessionInteractor.getEndDivider("<fart>"));
        assertEquals("</original_user_prompt>", GPTSessionInteractor.getEndDivider(GPTSessionInteractor.ORIG_PROMPT_DIVIDER));
    }
}