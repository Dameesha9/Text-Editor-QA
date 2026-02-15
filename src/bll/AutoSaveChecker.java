package bll;

public class AutoSaveChecker {

    private static final int AUTO_SAVE_THRESHOLD = 500;

    public static boolean shouldTriggerAutoSave(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        int wordCount = countWords(content);
        return wordCount > AUTO_SAVE_THRESHOLD;
    }

    public static int countWords(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }

        String trimmedText = text.trim();
        String[] words = trimmedText.split("\\s+");

        return words.length;
    }

    public static boolean shouldTriggerAutoSaveByWordCount(int wordCount) {
        return wordCount > AUTO_SAVE_THRESHOLD;
    }

    public static int getAutoSaveThreshold() {
        return AUTO_SAVE_THRESHOLD;
    }
}
