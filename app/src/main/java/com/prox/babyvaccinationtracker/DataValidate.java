package com.prox.babyvaccinationtracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidate {
    public DataValidate() {
    }

    public boolean isValidName(String name) {

        if (name.length() < 6 || name.length() > 255) {
            return false;
        }

        String nameRegex = "^[a-zA-Z0-9 ]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
    public boolean IsValidNameVN(String name){
        if (name.length() < 6 || name.length() > 255) {
            return false;
        }

        String nameRegex = "^[\\p{L}0-9 ]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
    public boolean isValidNameOrigin(String name) {
        String nameRegex = "^[a-zA-Z ]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }
    public boolean isValidNameOriginVN(String name){
        String nameRegex = "^[\\p{L} ]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    public boolean isValidEmail(String email) {
        if (email.length() < 6 || email.length() > 30) {
            return false;
        }

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pattern = Pattern.compile(emailRegex);

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        // Kiểm tra tất cả các điều kiện trong một lần duyệt
        return password.length() >= 8 &&
                containsCharacterType(password, CharacterType.UPPERCASE) &&
                containsCharacterType(password, CharacterType.LOWERCASE) &&
                containsCharacterType(password, CharacterType.DIGIT) &&
                containsCharacterType(password, CharacterType.SPECIAL_CHARACTER);
    }

    private static boolean containsCharacterType(String password, CharacterType type) {
        switch (type) {
            case UPPERCASE:
                return !password.equals(password.toLowerCase());
            case LOWERCASE:
                return !password.equals(password.toUpperCase());
            case DIGIT:
                return password.matches(".*\\d.*");
            case SPECIAL_CHARACTER:
                Pattern specialCharacterPattern = Pattern.compile("[!@#\\$%^&*()_+{}|:<>?~-]");
                Matcher matcher = specialCharacterPattern.matcher(password);
                return matcher.find();
            default:
                return false;
        }
    }

    private enum CharacterType {
        UPPERCASE,
        LOWERCASE,
        DIGIT,
        SPECIAL_CHARACTER
    }
}
