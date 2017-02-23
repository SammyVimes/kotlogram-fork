package ru.sofitlabs.telegram.transport;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;
import ru.sofitlabs.telegram.DataCenter;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * Created by Semyon on 23.02.2017.
 */
public interface MTProtoConnection {
    @NotNull
    String getTag();

    void setTag(@NotNull String var1);

    @NotNull
    Marker getMarker();

    @NotNull
    String getIp();

    int getPort();

    @NotNull
    DataCenter getDataCenter();

    @NotNull
    byte[] readMessage() throws Throwable;

    void writeMessage(@NotNull byte[] var1) throws IOException;

    @NotNull
    byte[] executeMethod(@NotNull byte[] var1) throws IOException;

    void close() throws IOException;

    boolean isOpen();

    @NotNull
    SelectionKey register(@NotNull Selector var1) throws IOException;

    @Nullable
    SelectionKey unregister() throws IOException;

    @Nullable
    SelectableChannel setBlocking(boolean var1) throws IOException;


}
