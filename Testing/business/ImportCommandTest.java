package testing.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import bll.ImportCommand;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

public class ImportCommandTest {

    @TempDir
    Path tempDir;

    @Test
    void testImportCommandSuccessWithTxtFile() throws Exception {
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Test content for import");
        }

        ImportCommand command = new ImportCommand(testFile, "test.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should succeed");
        assertTrue(command.isSuccess(), "Success flag should be true");
        assertTrue(command.getResult().contains("Test content for import"),
                  "Content should match");
    }

    @Test
    void testImportCommandSuccessWithMdFile() throws Exception {
        File testFile = tempDir.resolve("test.md").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("# Markdown Content\n\nTest markdown file.");
        }

        ImportCommand command = new ImportCommand(testFile, "test.md");
        boolean result = command.execute();

        assertTrue(result, "Import should succeed for .md files");
        assertTrue(command.getResult().contains("# Markdown Content"),
                  "Markdown content should be imported");
    }

    @Test
    void testImportCommandFailWithNullFile() {
        ImportCommand command = new ImportCommand(null, "test.txt");
        boolean result = command.execute();

        assertFalse(result, "Import should fail with null file");
        assertFalse(command.isSuccess(), "Success flag should be false");
    }

    @Test
    void testImportCommandFailWithNonExistentFile() {
        File nonExistent = new File("nonexistent_file.txt");

        ImportCommand command = new ImportCommand(nonExistent, "nonexistent_file.txt");
        boolean result = command.execute();

        assertFalse(result, "Import should fail for non-existent file");
        assertFalse(command.isSuccess(), "Success flag should be false");
    }

    @Test
    void testImportCommandFailWithNullFileName() throws Exception {
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Content");
        }

        ImportCommand command = new ImportCommand(testFile, null);
        boolean result = command.execute();

        assertFalse(result, "Import should fail with null filename");
    }

    @Test
    void testImportCommandFailWithEmptyFileName() throws Exception {
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Content");
        }

        ImportCommand command = new ImportCommand(testFile, "");
        boolean result = command.execute();

        assertFalse(result, "Import should fail with empty filename");
    }

    @Test
    void testImportCommandFailWithInvalidFileExtension() throws Exception {
        File testFile = tempDir.resolve("test.pdf").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("PDF Content");
        }

        ImportCommand command = new ImportCommand(testFile, "test.pdf");
        boolean result = command.execute();

        assertFalse(result, "Import should fail for non-txt/md files");
    }

    @Test
    void testImportCommandWithEmptyFile() throws Exception {
        File emptyFile = tempDir.resolve("empty.txt").toFile();
        emptyFile.createNewFile();

        ImportCommand command = new ImportCommand(emptyFile, "empty.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should succeed even with empty file");
        assertNotNull(command.getResult(), "Result should not be null");
    }

    @Test
    void testImportCommandWithMultilineContent() throws Exception {
        File testFile = tempDir.resolve("multiline.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Line 1\nLine 2\nLine 3\nLine 4");
        }

        ImportCommand command = new ImportCommand(testFile, "multiline.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should succeed");
        String content = command.getResult();
        assertTrue(content.contains("Line 1"), "Should contain line 1");
        assertTrue(content.contains("Line 4"), "Should contain line 4");
    }

    @Test
    void testImportCommandWithSpecialCharacters() throws Exception {
        File testFile = tempDir.resolve("special.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Special chars: !@#$%^&*()_+-={}[]|:;<>?,./");
        }

        ImportCommand command = new ImportCommand(testFile, "special.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should handle special characters");
        assertTrue(command.getResult().contains("!@#$%"),
                  "Special characters should be preserved");
    }

    @Test
    void testImportCommandWithUnicodeContent() throws Exception {
        File testFile = tempDir.resolve("unicode.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("العربية: مرحبا بك");
        }

        ImportCommand command = new ImportCommand(testFile, "unicode.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should handle Unicode");
        assertTrue(command.getResult().contains("مرحبا"),
                  "Unicode content should be preserved");
    }

    @Test
    void testImportCommandWithLargeFile() throws Exception {
        File largeFile = tempDir.resolve("large.txt").toFile();
        try (FileWriter writer = new FileWriter(largeFile)) {
            for (int i = 0; i < 10000; i++) {
                writer.write("Line " + i + "\n");
            }
        }

        ImportCommand command = new ImportCommand(largeFile, "large.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should handle large files");
        assertTrue(command.getResult().length() > 50000,
                  "Large file content should be imported");
    }

    @Test
    void testImportCommandMultipleExecutions() throws Exception {
        File testFile = tempDir.resolve("test.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Test content");
        }

        ImportCommand command = new ImportCommand(testFile, "test.txt");

        boolean result1 = command.execute();
        boolean result2 = command.execute();

        assertTrue(result1, "First execution should succeed");
        assertTrue(result2, "Second execution should succeed");
        assertEquals(command.getResult(), command.getResult(),
                    "Results should be consistent");
    }

    @Test
    void testImportCommandFileExtensionCaseInsensitive() throws Exception {
        File testFile = tempDir.resolve("test.TXT").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("Content");
        }

        ImportCommand command = new ImportCommand(testFile, "test.TXT");
        boolean result = command.execute();

        assertTrue(result, "Import should be case-insensitive for extensions");
    }

    @Test
    void testImportCommandWithWhitespaceContent() throws Exception {
        File testFile = tempDir.resolve("whitespace.txt").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("   \t\n   spaces and tabs   \n");
        }

        ImportCommand command = new ImportCommand(testFile, "whitespace.txt");
        boolean result = command.execute();

        assertTrue(result, "Import should succeed with whitespace");
        assertNotNull(command.getResult(), "Result should not be null");
    }
}
