package testing.data;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import dal.HashCalculator;

public class HashCalculatorTest {

    @Test
    void testHashConsistency() throws Exception {
        String text = "This is a test document";
        String hash1 = HashCalculator.calculateHash(text);
        String hash2 = HashCalculator.calculateHash(text);

        assertEquals(hash1, hash2,
                    "Same input should produce same hash");
    }

    @Test
    void testHashChangesWithDifferentContent() throws Exception {
        String text1 = "Original content";
        String text2 = "Modified content";

        String hash1 = HashCalculator.calculateHash(text1);
        String hash2 = HashCalculator.calculateHash(text2);

        assertNotEquals(hash1, hash2,
                       "Different content should produce different hashes");
    }

    @Test
    void testHashFormat() throws Exception {
        String text = "Test content";
        String hash = HashCalculator.calculateHash(text);

        assertNotNull(hash, "Hash should not be null");
        assertEquals(32, hash.length(),
                    "MD5 hash should be 32 characters long");
        assertTrue(hash.matches("[0-9A-F]+"),
                  "Hash should contain only hex characters (0-9, A-F)");
    }

    @Test
    void testHashWithEmptyString() throws Exception {
        String emptyText = "";
        String hash = HashCalculator.calculateHash(emptyText);

        assertNotNull(hash, "Hash of empty string should not be null");
        assertEquals(32, hash.length(), "Hash should still be 32 chars");
        assertEquals("D41D8CD98F00B204E9800998ECF8427E", hash,
                    "Empty string MD5 hash should match known value");
    }

    @Test
    void testHashWithSingleCharacter() throws Exception {
        String text = "a";
        String hash = HashCalculator.calculateHash(text);

        assertNotNull(hash, "Hash should not be null");
        assertEquals(32, hash.length(), "Hash should be 32 characters");
    }

    @Test
    void testHashWithLongContent() throws Exception {
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            longText.append("a");
        }

        String hash = HashCalculator.calculateHash(longText.toString());

        assertNotNull(hash, "Hash of long content should not be null");
        assertEquals(32, hash.length(),
                    "Hash should still be 32 characters for long content");
    }

    @Test
    void testHashWithSpecialCharacters() throws Exception {
        String text = "Special!@#$%^&*()_+-={}[]|:;<>?,./~`";
        String hash = HashCalculator.calculateHash(text);

        assertNotNull(hash, "Hash with special chars should not be null");
        assertEquals(32, hash.length(), "Hash should be 32 characters");
    }

    @Test
    void testHashWithUnicodeCharacters() throws Exception {
        String text = "مرحبا العالم";
        String hash = HashCalculator.calculateHash(text);

        assertNotNull(hash, "Hash with Unicode should not be null");
        assertEquals(32, hash.length(), "Hash should be 32 characters");
    }

    @Test
    void testHashWithNewlines() throws Exception {
        String text = "Line1\nLine2\nLine3";
        String hash = HashCalculator.calculateHash(text);

        assertNotNull(hash, "Hash with newlines should not be null");
        assertEquals(32, hash.length(), "Hash should be 32 characters");
    }

    @Test
    void testMinorContentChangeProducesDifferentHash() throws Exception {
        String text1 = "This is version 1";
        String text2 = "This is version 2";

        String hash1 = HashCalculator.calculateHash(text1);
        String hash2 = HashCalculator.calculateHash(text2);

        assertNotEquals(hash1, hash2,
                       "Minor change should produce different hash");
    }

    @Test
    void testHashIntegrityAfterEdit() throws Exception {
        String originalContent = "Original document content";
        String editedContent = "Edited document content";

        String originalHash = HashCalculator.calculateHash(originalContent);
        String editedHash = HashCalculator.calculateHash(editedContent);

        assertNotEquals(originalHash, editedHash,
                       "Editing content should change hash");

        String originalHashAgain = HashCalculator.calculateHash(originalContent);
        assertEquals(originalHash, originalHashAgain,
                    "Original hash should remain consistent");
    }

    @Test
    void testHashDeterminism() throws Exception {
        String text = "Deterministic test";
        String hash1 = HashCalculator.calculateHash(text);

        Thread.sleep(100);

        String hash2 = HashCalculator.calculateHash(text);

        assertEquals(hash1, hash2,
                    "Hash should be deterministic regardless of time");
    }

    @Test
    void testHashWithWhitespace() throws Exception {
        String text1 = "no spaces";
        String text2 = "no  spaces";
        String text3 = "no   spaces";

        String hash1 = HashCalculator.calculateHash(text1);
        String hash2 = HashCalculator.calculateHash(text2);
        String hash3 = HashCalculator.calculateHash(text3);

        assertNotEquals(hash1, hash2,
                       "Different whitespace should produce different hashes");
        assertNotEquals(hash2, hash3,
                       "Different whitespace should produce different hashes");
    }

    @Test
    void testHashCaseSensitivity() throws Exception {
        String lowercase = "lowercase text";
        String uppercase = "LOWERCASE TEXT";

        String hash1 = HashCalculator.calculateHash(lowercase);
        String hash2 = HashCalculator.calculateHash(uppercase);

        assertNotEquals(hash1, hash2,
                       "Hash should be case sensitive");
    }

    @Test
    void testMultipleHashCalculations() throws Exception {
        String text = "Test for multiple calculations";

        String hash1 = HashCalculator.calculateHash(text);
        String hash2 = HashCalculator.calculateHash(text);
        String hash3 = HashCalculator.calculateHash(text);
        String hash4 = HashCalculator.calculateHash(text);

        assertEquals(hash1, hash2, "All hashes should be equal");
        assertEquals(hash2, hash3, "All hashes should be equal");
        assertEquals(hash3, hash4, "All hashes should be equal");
    }
}
