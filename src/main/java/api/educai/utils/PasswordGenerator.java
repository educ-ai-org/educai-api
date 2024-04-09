package api.educai.utils;

import java.util.Random;

public class PasswordGenerator {
    private static final String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String generate(int size) {
        StringBuilder senha = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            int randomIndex = random.nextInt(validCharacters.length());
            char randomCharacter = validCharacters.charAt(randomIndex);
            senha.append(randomCharacter);
        }

        return senha.toString();
    }
}
