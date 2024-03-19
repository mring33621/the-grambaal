package xyz.mattring.grambaal.convo;

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ConvoUtils {

    public static final String SYSTEM_PROMPT_DIVIDER = ChunkType.SystemPrompt.getAsDividerTag();
    public static final String ORIG_PROMPT_DIVIDER = ChunkType.OriginalUserPrompt.getAsDividerTag();
    public static final String GPT_RESP_DIVIDER = ChunkType.AssistantResponse.getAsDividerTag();
    public static final String USER_FOLLOWUP_DIVIDER = ChunkType.UserFollowup.getAsDividerTag();

    private static final String CRLF = "\r\n";

    public static String annotateDivider(String divider, String modelName) {
        String annotatedDivider = divider;
        if (GPT_RESP_DIVIDER.equals(divider)) {
            String modelAnno = java.lang.String.format(" model=\"%s\"", modelName);
            annotatedDivider = annotatedDivider.replace(">", modelAnno + ">");
        }
        final LocalDateTime now = LocalDateTime.now();
        // Format the LocalDateTime object using the ISO 8601 date format
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        final String formattedDateTime = formatter.format(now);
        annotatedDivider = annotatedDivider.replace(">", " timestamp=\"" + formattedDateTime + "\">");
        return annotatedDivider;
    }

    public static String getEndDivider(String divider) {
        return divider.replace("<", "</");
    }

    public static List<Chunk> splitConvo(String taggedDividedConvoText) {
        List<Chunk> chunks = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new StringReader(taggedDividedConvoText))) {
            String line;
            String currentTag = null;
            ChunkType currentType = null;
            StringBuilder currentText = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                line = ConvoUtils.stripControlChars(line);
                Optional<ChunkType> chunkType = ChunkType.fromTag(line);
                if (chunkType.isPresent()) {
                    if (currentTag != null) {
                        chunks.add(new Chunk(currentTag, currentType, currentText.toString()));
                        currentTag = null;
                        currentType = null;
                        currentText = new StringBuilder();
                    } else {
                        currentTag = line;
                        currentType = chunkType.get();
                    }
                } else {
                    currentText.append(line).append(CRLF);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return chunks;
    }

    public static String joinConvo(List<Chunk> chunks) {
        final StringBuilder convo = new StringBuilder();
        for (Chunk chunk : chunks) {
            convo.append(chunk.tag()).append(CRLF);
            convo.append(chunk.text());
            convo.append(ConvoUtils.getEndDivider(chunk.type().getAsDividerTag())).append(CRLF);
        }
        return convo.toString();
    }

    public static String stripControlChars(String input) {
        return input.replaceAll("\\p{C}", "");
    }

}
