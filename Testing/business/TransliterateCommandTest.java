package testing.business;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import bll.TransliterateCommand;

public class TransliterateCommandTest {

    @Test
    void testTransliterateCommandWithArabicText() {
        String arabicText = "مرحبا";

        TransliterateCommand command = new TransliterateCommand(arabicText);
        boolean result = command.execute();

        assertTrue(result, "Transliteration should succeed");
        assertTrue(command.isSuccess(), "Success flag should be true");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandResultNotEmpty() {
        String arabicText = "السلام عليكم";

        TransliterateCommand command = new TransliterateCommand(arabicText);
        command.execute();

        String result = command.getResult();
        assertNotNull(result, "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithEmptyString() {
        TransliterateCommand command = new TransliterateCommand("");
        boolean result = command.execute();

        assertTrue(result, "Should handle empty string gracefully");
        assertEquals("", command.getResult(), "Result should be empty");
    }

    @Test
    void testTransliterateCommandWithNull() {
        TransliterateCommand command = new TransliterateCommand(null);
        boolean result = command.execute();

        assertTrue(result, "Should handle null gracefully");
        assertEquals("", command.getResult(), "Result should be empty for null");
    }

    @Test
    void testTransliterateCommandWithWhitespace() {
        TransliterateCommand command = new TransliterateCommand("   ");
        boolean result = command.execute();

        assertTrue(result, "Should handle whitespace");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandConsistency() {
        String arabicText = "الله";

        TransliterateCommand command1 = new TransliterateCommand(arabicText);
        command1.execute();
        String result1 = command1.getResult();

        TransliterateCommand command2 = new TransliterateCommand(arabicText);
        command2.execute();
        String result2 = command2.getResult();

        assertEquals(result1, result2,
                    "Same input should produce same transliteration");
    }

    @Test
    void testTransliterateCommandWithMixedContent() {
        String mixedText = "Hello مرحبا World";

        TransliterateCommand command = new TransliterateCommand(mixedText);
        boolean result = command.execute();

        assertTrue(result, "Should handle mixed Arabic and English");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithNumbers() {
        String textWithNumbers = "١٢٣ ٤٥٦";

        TransliterateCommand command = new TransliterateCommand(textWithNumbers);
        boolean result = command.execute();

        assertTrue(result, "Should handle Arabic numerals");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithSpecialCharacters() {
        String textWithSpecial = "مرحبا! كيف الحال؟";

        TransliterateCommand command = new TransliterateCommand(textWithSpecial);
        boolean result = command.execute();

        assertTrue(result, "Should handle punctuation");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithLongText() {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longText.append("مرحبا بك في النظام ");
        }

        TransliterateCommand command = new TransliterateCommand(longText.toString());
        boolean result = command.execute();

        assertTrue(result, "Should handle long text");
        assertTrue(command.getResult().length() > 0,
                  "Result should contain transliterated content");
    }

    @Test
    void testTransliterateCommandWithSingleCharacter() {
        TransliterateCommand command = new TransliterateCommand("أ");
        boolean result = command.execute();

        assertTrue(result, "Should handle single character");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithDiacritics() {
        String textWithDiacritics = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";

        TransliterateCommand command = new TransliterateCommand(textWithDiacritics);
        boolean result = command.execute();

        assertTrue(result, "Should handle diacritics");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandMultipleExecutions() {
        String arabicText = "العالم";

        TransliterateCommand command = new TransliterateCommand(arabicText);

        boolean result1 = command.execute();
        boolean result2 = command.execute();

        assertTrue(result1, "First execution should succeed");
        assertTrue(result2, "Second execution should succeed");
    }

    @Test
    void testTransliterateCommandSuccessFlag() {
        TransliterateCommand command = new TransliterateCommand("مرحبا");

        assertFalse(command.isSuccess(), "Success should be false before execution");

        command.execute();

        assertTrue(command.isSuccess(), "Success should be true after execution");
    }

    @Test
    void testTransliterateCommandWithNewlines() {
        String multilineText = "مرحبا\nكيف حالك\nأهلا";

        TransliterateCommand command = new TransliterateCommand(multilineText);
        boolean result = command.execute();

        assertTrue(result, "Should handle newlines");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandWithTabs() {
        String textWithTabs = "مرحبا\t\tبك";

        TransliterateCommand command = new TransliterateCommand(textWithTabs);
        boolean result = command.execute();

        assertTrue(result, "Should handle tabs");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testTransliterateCommandDifferentInputsDifferentOutputs() {
        TransliterateCommand command1 = new TransliterateCommand("مرحبا");
        command1.execute();

        TransliterateCommand command2 = new TransliterateCommand("السلام");
        command2.execute();

        assertNotEquals(command1.getResult(), command2.getResult(),
                       "Different inputs should produce different results");
    }
}
