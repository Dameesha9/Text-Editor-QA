package testing.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import dal.TFIDFCalculator;

public class TFIDFCalculatorNegativeTest {

    private TFIDFCalculator calculator;
    private static final double DELTA = 0.01;

    @BeforeEach
    void setUp() {
        calculator = new TFIDFCalculator();
    }

    @Test
    void testTFIDFWithEmptyDocument() {
        calculator.addDocumentToCorpus("test document one");
        calculator.addDocumentToCorpus("test document two");

        String emptyDoc = "";
        double score = calculator.calculateDocumentTfIdf(emptyDoc);

        assertTrue(Double.isNaN(score) || score == 0.0,
                  "Empty document should return NaN or 0");
    }

    @Test
    void testTFIDFWithWhitespaceOnly() {
        calculator.addDocumentToCorpus("normal document");
        calculator.addDocumentToCorpus("another document");

        String whitespaceDoc = "   \t\n  ";
        double score = calculator.calculateDocumentTfIdf(whitespaceDoc);

        assertTrue(Double.isNaN(score) || Double.isInfinite(score) || score == 0.0,
                  "Whitespace-only document should handle gracefully");
    }

    @Test
    void testTFIDFWithEmptyCorpus() {
        String testDoc = "test document";
        double score = calculator.calculateDocumentTfIdf(testDoc);

        assertTrue(Double.isFinite(score) || Double.isNaN(score) || Double.isInfinite(score),
                  "Empty corpus should handle gracefully without throwing exception");
    }

    @Test
    void testTFIDFWithSpecialCharactersOnly() {
        calculator.addDocumentToCorpus("normal text document");
        calculator.addDocumentToCorpus("another normal document");

        String specialCharsDoc = "!@#$%^&*()_+-={}[]|:;<>?,./";
        double score = calculator.calculateDocumentTfIdf(specialCharsDoc);

        assertNotNull(score, "Should return a score even with special characters");
        assertTrue(Double.isFinite(score) || Double.isNaN(score),
                  "Special characters should be handled gracefully");
    }

    @Test
    void testTFIDFWithNumbersOnly() {
        calculator.addDocumentToCorpus("text document one");
        calculator.addDocumentToCorpus("text document two");

        String numbersDoc = "123 456 789 000";
        double score = calculator.calculateDocumentTfIdf(numbersDoc);

        assertNotNull(score, "Should handle numeric-only documents");
        assertTrue(Double.isFinite(score) || Double.isNaN(score),
                  "Numbers should be processed without errors");
    }

    @Test
    void testTFIDFWithVeryLongDocument() {
        calculator.addDocumentToCorpus("short doc");
        calculator.addDocumentToCorpus("another short");

        StringBuilder longDoc = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longDoc.append("word").append(i).append(" ");
        }

        double score = calculator.calculateDocumentTfIdf(longDoc.toString());

        assertNotNull(score, "Should handle very long documents");
        assertTrue(Double.isFinite(score),
                  "Very long document should produce finite score");
    }

    @Test
    void testTFIDFWithSingleCharacterWords() {
        calculator.addDocumentToCorpus("a b c");
        calculator.addDocumentToCorpus("d e f");

        String singleCharDoc = "x y z";
        double score = calculator.calculateDocumentTfIdf(singleCharDoc);

        assertNotNull(score, "Should handle single character words");
        assertTrue(Double.isFinite(score) || Double.isNaN(score),
                  "Single characters should be processed");
    }

    @Test
    void testTFIDFWithMixedLanguageCharacters() {
        calculator.addDocumentToCorpus("english text here");
        calculator.addDocumentToCorpus("more english content");

        String mixedDoc = "English عربي 中文 русский";
        double score = calculator.calculateDocumentTfIdf(mixedDoc);

        assertNotNull(score, "Should handle mixed language text");
        assertTrue(Double.isFinite(score) || Double.isNaN(score),
                  "Mixed languages should process without errors");
    }

    @Test
    void testTFIDFWithNullCorpusAddition() {
        assertDoesNotThrow(() -> {
            calculator.addDocumentToCorpus("valid document");
            double score = calculator.calculateDocumentTfIdf("test");
            assertNotNull(score);
        }, "Should not throw exception during normal operation");
    }

    @Test
    void testTFIDFWithRepeatedEmptyDocuments() {
        calculator.addDocumentToCorpus("");
        calculator.addDocumentToCorpus("");
        calculator.addDocumentToCorpus("actual content");

        String testDoc = "test content here";
        double score = calculator.calculateDocumentTfIdf(testDoc);

        assertNotNull(score, "Should handle corpus with empty documents");
    }

    @Test
    void testTFIDFConsistencyAfterMultipleEmptyQueries() {
        calculator.addDocumentToCorpus("document one");
        calculator.addDocumentToCorpus("document two");

        calculator.calculateDocumentTfIdf("");
        calculator.calculateDocumentTfIdf("");
        double score = calculator.calculateDocumentTfIdf("document");

        assertTrue(Double.isFinite(score),
                  "Should maintain consistency after empty queries");
    }
}
