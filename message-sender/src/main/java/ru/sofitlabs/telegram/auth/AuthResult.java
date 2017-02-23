package ru.sofitlabs.telegram.auth;

import org.jetbrains.annotations.NotNull;
import ru.sofitlabs.telegram.transport.MTProtoConnection;

/**
 * Created by Semyon on 23.02.2017.
 */
public final class AuthResult {
    @NotNull
    private final AuthKey authKey;
    private final long serverSalt;
    @NotNull
    private final MTProtoConnection connection;

    @NotNull
    public final AuthKey getAuthKey() {
        return this.authKey;
    }

    public final long getServerSalt() {
        return this.serverSalt;
    }

    @NotNull
    public final MTProtoConnection getConnection() {
        return this.connection;
    }

    public AuthResult(@NotNull AuthKey authKey, long serverSalt, @NotNull MTProtoConnection connection) {
        this.authKey = authKey;
        this.serverSalt = serverSalt;
        this.connection = connection;
    }
}
