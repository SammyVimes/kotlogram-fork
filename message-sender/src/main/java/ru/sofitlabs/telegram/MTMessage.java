package ru.sofitlabs.telegram;

import com.github.badoualy.telegram.tl.TLContext;
import com.github.badoualy.telegram.tl.core.TLObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.github.badoualy.telegram.tl.StreamUtils.*;

/**
 * Created by Semyon on 23.02.2017.
 */
public class MTMessage extends TLObject {

    private Long messageId = 0L;
    private int seqNo = 0;
    private byte[] payload = new byte[0];
    private int payloadLength = payload.length;

    public MTMessage(final Long messageId, final int seqNo, final byte[] payload) {
        this.messageId = messageId;
        this.seqNo = seqNo;
        this.payload = payload;
        this.payloadLength = payload.length;
    }

    @Override
    public void serializeBody(final OutputStream stream) throws IOException {
        writeLong(messageId, stream);
        writeInt(seqNo, stream);
        writeInt(payloadLength, stream);
        writeByteArray(payload, 0, payloadLength, stream);
    }

    @Override
    public void deserializeBody(final InputStream stream, final TLContext context) throws IOException {
        messageId = readLong(stream);
        seqNo = readInt(stream);
        payloadLength = readInt(stream);
        payload = new byte[payloadLength];
        readBytes(payload, 0, payloadLength, stream);
    }

    @Override
    public int getConstructorId() {
        return 0;
    }

    @Override
    public String toString() {
        return "MTMessage: {id: " + messageId + ", seqNo: " + seqNo + "}";
    }
}
