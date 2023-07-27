package com.example.one_vote_service.utils;

import java.util.Random;

public class RandomUtils {
    public static String generateRandomString() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789---";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 30; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public static String generateRandomPassword() {
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();
    }

}
