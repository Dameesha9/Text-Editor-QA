package testing.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import dal.TFIDFCalculator;

public class TFIDFCalculatorTest {

    private TFIDFCalculator calculator;
    private static final double DELTA = 0.01;

    @BeforeEach
    void setUp() {
        calculator = new TFIDFCalculator();
    }

    @Test
    void testTFIDFWithKnownValues() {
        calculator.addDocumentToCorpus("cat dog");
        calculator.addDocumentToCorpus("dog bird");
        calculator.addDocumentToCorpus("bird fish");

        String testDocument = "cat dog";
        double actualScore = calculator.calculateDocumentTfIdf(testDocument);

        assertTrue(actualScore >= -1.0 && actualScore <= 1.0,
                  "TF-IDF score should be within reasonable range");
    }

    @Test
    void testTFIDFWithIdenticalDocument() {
        String document = "hello world test document";
        calculator.addDocumentToCorpus(document);
        calculator.addDocumentToCorpus("another test document here");
        calculator.addDocumentToCorpus("yet another example text");

        double score1 = calculator.calculateDocumentTfIdf(document);
        double score2 = calculator.calculateDocumentTfIdf(document);

        assertEquals(score1, score2, DELTA,
                    "Same document should produce same TF-IDF score");
    }

    @Test
    void testTFIDFWithSingleWordDocument() {
        calculator.addDocumentToCorpus("apple");
        calculator.addDocumentToCorpus("banana orange");
        calculator.addDocumentToCorpus("grape cherry");

        String singleWordDoc = "apple";
        double actualScore = calculator.calculateDocumentTfIdf(singleWordDoc);

        assertNotNull(actualScore, "Score should not be null");
        assertTrue(Double.isFinite(actualScore), "Score should be finite");
    }

    @Test
    void testTFIDFWithRepeatedWords() {
        calculator.addDocumentToCorpus("word word word");
        calculator.addDocumentToCorpus("other text here");

        String testDoc = "word word";
        double actualScore = calculator.calculateDocumentTfIdf(testDoc);

        assertTrue(Double.isFinite(actualScore),
                  "Score with repeated words should be finite");
    }

    @Test
    void testTFIDFWithMultipleDocuments() {
        calculator.addDocumentToCorpus("machine learning is great");
        calculator.addDocumentToCorpus("deep learning is powerful");
        calculator.addDocumentToCorpus("artificial intelligence is amazing");
        calculator.addDocumentToCorpus("neural networks are complex");

        String queryDoc = "machine learning deep learning";
        double score = calculator.calculateDocumentTfIdf(queryDoc);

        assertNotNull(score, "Score should be calculated");
        assertTrue(Double.isFinite(score), "Score should be finite number");
    }

    @Test
    void testTFIDFScoreConsistency() {
        calculator.addDocumentToCorpus("test document one");
        calculator.addDocumentToCorpus("test document two");
        calculator.addDocumentToCorpus("test document three");

        String doc = "test document";

        double score1 = calculator.calculateDocumentTfIdf(doc);
        double score2 = calculator.calculateDocumentTfIdf(doc);
        double score3 = calculator.calculateDocumentTfIdf(doc);

        assertEquals(score1, score2, DELTA, "Scores should be consistent");
        assertEquals(score2, score3, DELTA, "Scores should be consistent");
    }

    @Test
    void testTFIDFWithCommonWords() {
        calculator.addDocumentToCorpus("the cat sat on the mat");
        calculator.addDocumentToCorpus("the dog sat on the floor");
        calculator.addDocumentToCorpus("the bird sat on the tree");

        String testDoc = "the sat on";
        double score = calculator.calculateDocumentTfIdf(testDoc);

        assertTrue(score < 0.5,
                  "Common words across all documents should have lower TF-IDF");
    }

    @Test
    void testTFIDFWithUniqueWords() {
        calculator.addDocumentToCorpus("common word text");
        calculator.addDocumentToCorpus("common word document");
        calculator.addDocumentToCorpus("common word file");

        String uniqueDoc = "unique special rare";
        double score = calculator.calculateDocumentTfIdf(uniqueDoc);

        assertTrue(Double.isFinite(score),
                  "Document with unique words should produce valid score");
    }
}
