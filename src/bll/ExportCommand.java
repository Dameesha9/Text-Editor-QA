package bll;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ExportCommand implements Command {

    private String filePath;
    private String content;
    private boolean success;

    public ExportCommand(String filePath, String content) {
        this.filePath = filePath;
        this.content = content;
        this.success = false;
    }

    @Override
    public boolean execute() {
        if (filePath == null || filePath.trim().isEmpty()) {
            return false;
        }

        if (content == null) {
            content = "";
        }

        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    return false;
                }
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(content);
                success = true;
                return true;
            }

        } catch (Exception e) {
            success = false;
            return false;
        }
    }

    @Override
    public String getResult() {
        return success ? "File exported successfully to: " + filePath : "Export failed";
    }

    public boolean isSuccess() {
        return success;
    }
}
