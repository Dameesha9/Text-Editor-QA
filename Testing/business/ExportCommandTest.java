package testing.business;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import bll.ExportCommand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;

public class ExportCommandTest {

    @TempDir
    Path tempDir;

    @Test
    void testExportCommandSuccess() {
        String filePath = tempDir.resolve("export_test.txt").toString();
        String content = "Test content for export";

        ExportCommand command = new ExportCommand(filePath, content);
        boolean result = command.execute();

        assertTrue(result, "Export should succeed");
        assertTrue(command.isSuccess(), "Success flag should be true");
        assertTrue(new File(filePath).exists(), "File should be created");
    }

    @Test
    void testExportCommandVerifyContent() throws Exception {
        String filePath = tempDir.resolve("verify_content.txt").toString();
        String content = "Specific content to verify";

        ExportCommand command = new ExportCommand(filePath, content);
        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String fileContent = reader.readLine();
            assertEquals(content, fileContent, "File content should match");
        }
    }

    @Test
    void testExportCommandWithEmptyContent() {
        String filePath = tempDir.resolve("empty_export.txt").toString();

        ExportCommand command = new ExportCommand(filePath, "");
        boolean result = command.execute();

        assertTrue(result, "Export should succeed with empty content");
        assertTrue(new File(filePath).exists(), "File should be created");
    }

    @Test
    void testExportCommandWithNullContent() {
        String filePath = tempDir.resolve("null_content.txt").toString();

        ExportCommand command = new ExportCommand(filePath, null);
        boolean result = command.execute();

        assertTrue(result, "Export should succeed with null content");
        assertTrue(new File(filePath).exists(), "File should be created");
    }

    @Test
    void testExportCommandFailWithNullFilePath() {
        ExportCommand command = new ExportCommand(null, "Content");
        boolean result = command.execute();

        assertFalse(result, "Export should fail with null file path");
        assertFalse(command.isSuccess(), "Success flag should be false");
    }

    @Test
    void testExportCommandFailWithEmptyFilePath() {
        ExportCommand command = new ExportCommand("", "Content");
        boolean result = command.execute();

        assertFalse(result, "Export should fail with empty file path");
    }

    @Test
    void testExportCommandCreatesDirectories() {
        String filePath = tempDir.resolve("subdir/nested/export.txt").toString();

        ExportCommand command = new ExportCommand(filePath, "Nested content");
        boolean result = command.execute();

        assertTrue(result, "Export should create parent directories");
        assertTrue(new File(filePath).exists(), "File should exist in nested directory");
    }

    @Test
    void testExportCommandWithMultilineContent() throws Exception {
        String filePath = tempDir.resolve("multiline_export.txt").toString();
        String content = "Line 1\nLine 2\nLine 3";

        ExportCommand command = new ExportCommand(filePath, content);
        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder fileContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            assertTrue(fileContent.toString().contains("Line 1"),
                      "Should contain line 1");
            assertTrue(fileContent.toString().contains("Line 3"),
                      "Should contain line 3");
        }
    }

    @Test
    void testExportCommandWithSpecialCharacters() throws Exception {
        String filePath = tempDir.resolve("special_chars.txt").toString();
        String content = "Special: !@#$%^&*()_+-={}[]|:;<>?,./";

        ExportCommand command = new ExportCommand(filePath, content);
        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String fileContent = reader.readLine();
            assertEquals(content, fileContent,
                        "Special characters should be preserved");
        }
    }

    @Test
    void testExportCommandWithUnicodeContent() throws Exception {
        String filePath = tempDir.resolve("unicode_export.txt").toString();
        String content = "العربية: مرحبا بك في النظام";

        ExportCommand command = new ExportCommand(filePath, content);
        command.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String fileContent = reader.readLine();
            assertTrue(fileContent.contains("مرحبا"),
                      "Unicode content should be preserved");
        }
    }

    @Test
    void testExportCommandOverwritesExistingFile() throws Exception {
        String filePath = tempDir.resolve("overwrite_test.txt").toString();

        ExportCommand command1 = new ExportCommand(filePath, "Original content");
        command1.execute();

        ExportCommand command2 = new ExportCommand(filePath, "New content");
        command2.execute();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String fileContent = reader.readLine();
            assertEquals("New content", fileContent,
                        "File should be overwritten");
        }
    }

    @Test
    void testExportCommandWithLargeContent() {
        String filePath = tempDir.resolve("large_export.txt").toString();
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeContent.append("Line ").append(i).append("\n");
        }

        ExportCommand command = new ExportCommand(filePath, largeContent.toString());
        boolean result = command.execute();

        assertTrue(result, "Export should handle large content");
        File file = new File(filePath);
        assertTrue(file.length() > 50000, "Large file should be created");
    }

    @Test
    void testExportCommandGetResultMessage() {
        String filePath = tempDir.resolve("result_test.txt").toString();

        ExportCommand command = new ExportCommand(filePath, "Content");
        command.execute();

        String result = command.getResult();
        assertTrue(result.contains("success"), "Result message should indicate success");
        assertTrue(result.contains(filePath), "Result should contain file path");
    }

    @Test
    void testExportCommandFailureResultMessage() {
        ExportCommand command = new ExportCommand(null, "Content");
        command.execute();

        String result = command.getResult();
        assertTrue(result.toLowerCase().contains("fail"),
                  "Result should indicate failure");
    }

    @Test
    void testExportCommandMultipleExecutions() {
        String filePath = tempDir.resolve("multiple_exec.txt").toString();

        ExportCommand command = new ExportCommand(filePath, "Content");

        boolean result1 = command.execute();
        boolean result2 = command.execute();

        assertTrue(result1, "First execution should succeed");
        assertTrue(result2, "Second execution should succeed");
        assertTrue(new File(filePath).exists(), "File should exist");
    }

    @Test
    void testExportCommandWithWhitespaceOnlyContent() throws Exception {
        String filePath = tempDir.resolve("whitespace_export.txt").toString();
        String content = "   \t\n   ";

        ExportCommand command = new ExportCommand(filePath, content);
        command.execute();

        assertTrue(new File(filePath).exists(),
                  "File should be created with whitespace content");
    }
}
