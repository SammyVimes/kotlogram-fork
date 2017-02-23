package ru.sofitlabs.telegram.auth;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Semyon on 23.02.2017.
 */
public final class TempAuthKey extends AuthKey {
    private final int expiresAt;

    public final int getExpiresAt() {
        return this.expiresAt;
    }

    public TempAuthKey(@NotNull byte[] key, int expiresAt) throws Throwable {
        super(key);
        this.expiresAt = expiresAt;
    }
}
