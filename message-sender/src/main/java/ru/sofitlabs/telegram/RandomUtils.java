package ru.sofitlabs.telegram;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Semyon on 23.02.2017.
 */
public class RandomUtils {

    private static final SecureRandom random = new SecureRandom();

    static {
        random.setSeed(System.currentTimeMillis());
    }

    public static synchronized byte[] randomByteArray(int byteCount) {
        byte[] byteArray = new byte[byteCount];
        random.nextBytes(byteArray);
        return byteArray;
    }

    public static synchronized int randomInt() {
        return random.nextInt();
    }

    public static synchronized byte[] randomSessionId() {
        return randomByteArray(8);
    }

    public static synchronized long randomLong() {
        return (new BigInteger(randomByteArray(8))).longValueExact();
    }

    public static synchronized byte[] randomInt128() {
        return randomByteArray(16);
    }

    public static synchronized byte[] randomInt256() {
        return randomByteArray(32);
    }


}
