package org.e11eman.crackutilities.utilities;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class SecureRandomStuff {
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final List<String> CHARSET = Arrays.stream("QWERTYUIOPASDFGHJKLZXCVBNM1234567890".split("")).toList();

    public static String getRandomString(int charCount) {
        StringBuilder ranString = new StringBuilder();

        for (int i = 0; i < charCount; i++) ranString.append(CHARSET.get(SECURE_RANDOM.nextInt(CHARSET.size() - 1)));

        return ranString.toString().trim();
    }
}
