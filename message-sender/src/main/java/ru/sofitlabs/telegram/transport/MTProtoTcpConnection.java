package com.github.badoualy.telegram.mtproto.transport;

import com.github.badoualy.telegram.tl.ByteBufferUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import ru.sofitlabs.telegram.DataCenter;
import ru.sofitlabs.telegram.transport.MTProtoConnection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public final class MTProtoTcpConnection implements MTProtoConnection {
    private final int ATTEMPT_COUNT = 3;
    @NotNull
    private String tag;
    @NotNull
    private Marker marker;

    private SocketChannel socketChannel;

    private final ByteBuffer msgHeaderBuffer = ByteBuffer.allocate(1);

    private final ByteBuffer msgLengthBuffer = ByteBuffer.allocate(3);

    private SelectionKey selectionKey;

    @NotNull
    private final String ip;

    private final int port;

    private static final Logger logger = LoggerFactory.getLogger(MTProtoTcpConnection.class);

    public MTProtoTcpConnection(@NotNull String ip, int port, @NotNull String tag, boolean abridgedProtocol) throws IOException {
        super();
        this.ip = ip;
        this.port = port;
        this.tag = tag;

        this.marker = MarkerFactory.getMarker(tag);
        int attempt = 1;

        while (true) {
            SocketChannel sockChannel = SocketChannel.open();
            this.socketChannel = sockChannel;
            this.socketChannel.setOption(StandardSocketOptions.SO_KEEPALIVE, Boolean.TRUE);
            this.socketChannel.configureBlocking(true);

            try {
                this.socketChannel.connect(new InetSocketAddress(this.getIp(), this.getPort()));
                this.socketChannel.finishConnect();
                if (abridgedProtocol) {
                    logger.info(this.getMarker(), "Using abridged protocol");
                    this.socketChannel.write(ByteBuffer.wrap(new byte[]{(byte) 239}));
                }

                logger.info(this.getMarker(), "Connected to " + this.getIp() + ":" + this.getPort() + " isConnected: " + this.socketChannel.isConnected() + ", isOpen: " + this.socketChannel.isOpen());
                break;
            } catch (Exception var9) {
                logger.error(this.getMarker(), "Failed to connect");

                try {
                    this.socketChannel.close();
                } catch (Exception ignored) {
                }

                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
                } catch (InterruptedException ignored) {

                }
                if (attempt == this.ATTEMPT_COUNT) {
                    throw new ConnectException("Failed to join Telegram server at " + this.getIp() + ":" + this.getPort());
                }

                if (attempt++ >= this.ATTEMPT_COUNT) {
                    break;
                }
            }
        }

    }

    public MTProtoTcpConnection(@NotNull String ip, int port, @NotNull String tag) throws IOException {
        this(ip, port, tag, false);
    }

    @NotNull
    public String getTag() {
        return this.tag;
    }

    public void setTag(@NotNull String value) {
        this.tag = value;
        Marker marker = MarkerFactory.getMarker(this.getTag());
        this.setMarker(marker);
    }

    @NotNull
    public Marker getMarker() {
        return this.marker;
    }

    private void setMarker(Marker var1) {
        this.marker = var1;
    }

    @NotNull
    public byte[] readMessage() throws IOException {
        int length = ByteBufferUtils.readByteAsInt(readBytes(this, 1, this.msgHeaderBuffer, null, 4));
        if (length == 127) {
            length = ByteBufferUtils.readInt24(readBytes(this, 3, this.msgLengthBuffer, null, 4));
        }

        logger.debug(this.getMarker(), "About to read a message of length " + length * 4);
        ByteBuffer buffer = readBytes(this, length * 4, null, null, 6);
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes, 0, buffer.remaining());
        return bytes;
    }

    public void writeMessage(@NotNull byte[] request) throws IOException {
        int length = request.length / 4;
        int headerLength = length >= 127 ? 4 : 1;
        int totalLength = request.length + headerLength;
        ByteBuffer buffer = ByteBuffer.allocate(totalLength);
        if (headerLength == 4) {
            ByteBufferUtils.writeByte(127, buffer);
            ByteBufferUtils.writeInt24(length, buffer);
        } else {
            ByteBufferUtils.writeByte(length, buffer);
        }

        buffer.put(request);
        buffer.flip();
        this.writeBytes(buffer);
    }

    @NotNull
    public byte[] executeMethod(@NotNull byte[] request) throws IOException {
        this.writeMessage(request);
        return this.readMessage();
    }

    @NotNull
    public SelectionKey register(@NotNull Selector selector) throws IOException {
        this.socketChannel.configureBlocking(false);
        this.selectionKey = this.socketChannel.register(selector, SelectionKey.OP_READ);
        SelectionKey selKey = this.selectionKey;
        return selKey;
    }

    @Nullable
    public SelectionKey unregister() throws IOException {
        Selector selector = this.selectionKey != null ? this.selectionKey.selector() : null;
        SelectionKey var10000 = this.selectionKey;
        if (this.selectionKey != null) {
            var10000.cancel();
        }

        if (selector != null) {
            selector.wakeup();
        }

        this.socketChannel.configureBlocking(true);
        return this.selectionKey;
    }

    @NotNull
    public SelectableChannel setBlocking(boolean blocking) throws IOException {
        SelectableChannel var10000 = this.socketChannel.configureBlocking(blocking);
        return var10000;
    }

    public void close() throws IOException {
        logger.debug(this.getMarker(), "Closing connection");
        this.socketChannel.close();
    }

    public boolean isOpen() {
        return this.socketChannel.isOpen() && this.socketChannel.isConnected();
    }

    private final ByteBuffer readBytes(int length, ByteBuffer recycledBuffer, ByteOrder order) throws IOException {
        if (recycledBuffer != null) {
            recycledBuffer.clear();
        }

        ByteBuffer var10000 = recycledBuffer;
        if (recycledBuffer == null) {
            var10000 = ByteBuffer.allocate(length);
        }

        ByteBuffer buffer = var10000;
        buffer.order(order);

        int read;
        for (int totalRead = 0; totalRead < length; totalRead += read) {
            read = this.socketChannel.read(buffer);
            if (read == -1) {
                throw new IOException("Reached end-of-stream");
            }
        }

        buffer.flip();
        return buffer;
    }

    // $FF: synthetic method
    // $FF: bridge method
    static ByteBuffer readBytes(MTProtoTcpConnection connection, int length, ByteBuffer recBuffer, ByteOrder order, int var4) throws IOException {
        if ((var4 & 2) != 0) {
            recBuffer = null;
        }

        if ((var4 & 4) != 0) {
            ByteOrder var10003 = ByteOrder.BIG_ENDIAN;
            order = var10003;
        }

        return connection.readBytes(length, recBuffer, order);
    }

    private final void writeBytes(ByteBuffer buffer) throws IOException {
        while (buffer.hasRemaining()) {
            this.socketChannel.write(buffer);
        }

    }

    @NotNull
    public String getIp() {
        return this.ip;
    }

    public int getPort() {
        return this.port;
    }

    @NotNull
    public DataCenter getDataCenter() {
        return DataCenter.getDataCenter(this);
    }


}
