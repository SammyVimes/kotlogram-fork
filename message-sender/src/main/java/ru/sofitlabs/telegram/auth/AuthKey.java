package ru.sofitlabs.telegram.auth;

import org.jetbrains.annotations.NotNull;
import ru.sofitlabs.telegram.secure.CryptoUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Created by Semyon on 23.02.2017.
 */
public class AuthKey {
    private final byte[] keyId;
    @NotNull
    private final byte[] key;

    public final byte[] getKeyId() {
        return this.keyId;
    }

    public final long getKeyIdAsLong() {
        return (new BigInteger(this.keyId)).longValueExact();
    }

    @NotNull
    public final byte[] getKey() {
        return this.key;
    }

    public AuthKey(@NotNull byte[] key) throws Throwable {
        this.key = key;
        if(this.key.length != 256) {
            throw (Throwable)(new RuntimeException("AuthKey must be 256 Bytes found " + this.key.length + " bytes"));
        } else {
            this.keyId = CryptoUtils.substring(CryptoUtils.SHA1(this.key), 12, 8);
        }
    }

    public AuthKey(@NotNull ByteBuffer key) throws Throwable {
        this(key.array());
    }
}
