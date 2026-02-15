package bll;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ImportCommand implements Command {

    private File file;
    private String fileName;
    private String fileContent;
    private boolean success;

    public ImportCommand(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
        this.success = false;
        this.fileContent = "";
    }

    @Override
    public boolean execute() {
        if (file == null || !file.exists()) {
            return false;
        }

        if (fileName == null || fileName.trim().isEmpty()) {
            return false;
        }

        String fileExtension = getFileExtension(fileName);
        if (!fileExtension.equalsIgnoreCase("txt") && !fileExtension.equalsIgnoreCase("md")) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            fileContent = content.toString();
            success = true;
            return true;

        } catch (Exception e) {
            success = false;
            return false;
        }
    }

    @Override
    public String getResult() {
        return fileContent;
    }

    public boolean isSuccess() {
        return success;
    }

    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : fileName.substring(lastIndexOfDot + 1);
    }
}
