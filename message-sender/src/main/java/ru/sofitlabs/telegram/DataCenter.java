package ru.sofitlabs.telegram;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Semyon on 23.02.2017.
 */
public class DataCenter {

    @NotNull
    private final String ip;
    private final int port;


    public DataCenter(@NotNull String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @NotNull
    public String toString() {
        return this.ip + ":" + this.port;
    }

    @NotNull
    public final String getIp() {
        return this.ip;
    }

    public final int getPort() {
        return this.port;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DataCenter that = (DataCenter) o;

        if (port != that.port) return false;
        return ip.equals(that.ip);
    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }
}
