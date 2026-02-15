package bll;

import dal.Transliteration;

public class TransliterateCommand implements Command {

    private String arabicText;
    private String transliteratedText;
    private boolean success;

    public TransliterateCommand(String arabicText) {
        this.arabicText = arabicText;
        this.success = false;
        this.transliteratedText = "";
    }

    @Override
    public boolean execute() {
        if (arabicText == null || arabicText.trim().isEmpty()) {
            transliteratedText = "";
            success = true;
            return true;
        }

        try {
            transliteratedText = Transliteration.transliterate(arabicText);
            success = true;
            return true;

        } catch (Exception e) {
            success = false;
            transliteratedText = "";
            return false;
        }
    }

    @Override
    public String getResult() {
        return transliteratedText;
    }

    public boolean isSuccess() {
        return success;
    }
}
