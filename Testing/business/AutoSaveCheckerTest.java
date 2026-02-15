package testing.business;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import bll.AutoSaveChecker;

public class AutoSaveCheckerTest {

    private static final int THRESHOLD = 500;

    @Test
    void testAutoSaveNotTriggeredBelowThreshold() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 400; i++) {
            content.append("word ");
        }

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());

        assertFalse(shouldSave,
                   "Auto-save should not trigger when word count < 500");
    }

    @Test
    void testAutoSaveNotTriggeredAtExactThreshold() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            content.append("word ");
        }

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());

        assertFalse(shouldSave,
                   "Auto-save should not trigger at exactly 500 words");
    }

    @Test
    void testAutoSaveTriggeredAboveThreshold() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            content.append("word ");
        }

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());

        assertTrue(shouldSave,
                  "Auto-save should trigger when word count > 500");
    }

    @Test
    void testAutoSaveTriggeredSignificantlyAboveThreshold() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            content.append("word ");
        }

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());

        assertTrue(shouldSave,
                  "Auto-save should trigger for significantly more than 500 words");
    }

    @Test
    void testAutoSaveWithEmptyString() {
        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave("");

        assertFalse(shouldSave,
                   "Auto-save should not trigger for empty string");
    }

    @Test
    void testAutoSaveWithNull() {
        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(null);

        assertFalse(shouldSave,
                   "Auto-save should not trigger for null content");
    }

    @Test
    void testAutoSaveWithWhitespaceOnly() {
        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave("   \t\n   ");

        assertFalse(shouldSave,
                   "Auto-save should not trigger for whitespace-only content");
    }

    @Test
    void testWordCountAccuracy() {
        String content = "One two three four five";
        int wordCount = AutoSaveChecker.countWords(content);

        assertEquals(5, wordCount, "Should count 5 words");
    }

    @Test
    void testWordCountWithMultipleSpaces() {
        String content = "Word1    Word2     Word3";
        int wordCount = AutoSaveChecker.countWords(content);

        assertEquals(3, wordCount,
                    "Should count 3 words despite multiple spaces");
    }

    @Test
    void testWordCountWithNewlines() {
        String content = "Line1\nLine2\nLine3\nLine4\nLine5";
        int wordCount = AutoSaveChecker.countWords(content);

        assertEquals(5, wordCount, "Should count words separated by newlines");
    }

    @Test
    void testWordCountBoundary499Words() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 499; i++) {
            content.append("word ");
        }

        int wordCount = AutoSaveChecker.countWords(content.toString());
        assertEquals(499, wordCount, "Should count exactly 499 words");

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());
        assertFalse(shouldSave, "Should not trigger at 499 words");
    }

    @Test
    void testWordCountBoundary500Words() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            content.append("word ");
        }

        int wordCount = AutoSaveChecker.countWords(content.toString());
        assertEquals(500, wordCount, "Should count exactly 500 words");

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());
        assertFalse(shouldSave, "Should not trigger at exactly 500 words");
    }

    @Test
    void testWordCountBoundary501Words() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            content.append("word ");
        }

        int wordCount = AutoSaveChecker.countWords(content.toString());
        assertEquals(501, wordCount, "Should count exactly 501 words");

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());
        assertTrue(shouldSave, "Should trigger at 501 words");
    }

    @Test
    void testAutoSaveByWordCountDirectly() {
        assertFalse(AutoSaveChecker.shouldTriggerAutoSaveByWordCount(499),
                   "Should not trigger for 499 words");
        assertFalse(AutoSaveChecker.shouldTriggerAutoSaveByWordCount(500),
                   "Should not trigger for 500 words");
        assertTrue(AutoSaveChecker.shouldTriggerAutoSaveByWordCount(501),
                  "Should trigger for 501 words");
        assertTrue(AutoSaveChecker.shouldTriggerAutoSaveByWordCount(1000),
                  "Should trigger for 1000 words");
    }

    @Test
    void testGetAutoSaveThreshold() {
        int threshold = AutoSaveChecker.getAutoSaveThreshold();

        assertEquals(500, threshold,
                    "Auto-save threshold should be 500 words");
    }

    @Test
    void testAutoSaveWithUnicodeCharacters() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 501; i++) {
            content.append("كلمة ");
        }

        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave(content.toString());

        assertTrue(shouldSave,
                  "Should trigger auto-save with Unicode text > 500 words");
    }

    @Test
    void testAutoSaveWithMixedContent() {
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < 400; i++) {
            content.append("word ");
        }
        content.append("Some additional content with many words here ");
        for (int i = 0; i < 100; i++) {
            content.append("more ");
        }

        int wordCount = AutoSaveChecker.countWords(content.toString());

        assertTrue(wordCount > 500, "Total word count should exceed 500");
        assertTrue(AutoSaveChecker.shouldTriggerAutoSave(content.toString()),
                  "Should trigger auto-save");
    }

    @Test
    void testAutoSaveWithSingleWord() {
        boolean shouldSave = AutoSaveChecker.shouldTriggerAutoSave("word");

        assertFalse(shouldSave,
                   "Single word should not trigger auto-save");
    }

    @Test
    void testCountWordsWithLeadingTrailingSpaces() {
        String content = "   word1 word2 word3   ";
        int wordCount = AutoSaveChecker.countWords(content);

        assertEquals(3, wordCount,
                    "Should correctly count words ignoring leading/trailing spaces");
    }
}
