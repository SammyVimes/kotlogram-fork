package com.github.badoualy.telegram.tl.api.request;

import com.github.badoualy.telegram.tl.TLContext;
import com.github.badoualy.telegram.tl.core.TLBool;
import com.github.badoualy.telegram.tl.core.TLBytes;
import com.github.badoualy.telegram.tl.core.TLMethod;
import com.github.badoualy.telegram.tl.core.TLObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.github.badoualy.telegram.tl.StreamUtils.readInt;
import static com.github.badoualy.telegram.tl.StreamUtils.readLong;
import static com.github.badoualy.telegram.tl.StreamUtils.readTLBytes;
import static com.github.badoualy.telegram.tl.StreamUtils.readTLObject;
import static com.github.badoualy.telegram.tl.StreamUtils.writeInt;
import static com.github.badoualy.telegram.tl.StreamUtils.writeLong;
import static com.github.badoualy.telegram.tl.StreamUtils.writeTLBytes;

/**
 * @author Yannick Badoual yann.badoual@gmail.com
 * @see <a href="http://github.com/badoualy/kotlogram">http://github.com/badoualy/kotlogram</a>
 */
public class TLRequestUploadSaveBigFilePart extends TLMethod<TLBool> {
    public static final int CLASS_ID = 0xde7b673d;

    protected long fileId;

    protected int filePart;

    protected int fileTotalParts;

    protected TLBytes bytes;

    public TLRequestUploadSaveBigFilePart() {
    }

    public TLRequestUploadSaveBigFilePart(long fileId, int filePart, int fileTotalParts, TLBytes bytes) {
        this.fileId = fileId;
        this.filePart = filePart;
        this.fileTotalParts = fileTotalParts;
        this.bytes = bytes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TLBool deserializeResponse(InputStream stream, TLContext context) throws IOException {
        final TLObject response = readTLObject(stream, context);
        if (response == null) {
            throw new IOException("Unable to parse response");
        }
        if (!(response instanceof TLBool)) {
            throw new IOException("Incorrect response type, expected getClass().getCanonicalName(), found response.getClass().getCanonicalName()");
        }
        return (TLBool) response;
    }

    @Override
    public void serializeBody(OutputStream stream) throws IOException {
        writeLong(fileId, stream);
        writeInt(filePart, stream);
        writeInt(fileTotalParts, stream);
        writeTLBytes(bytes, stream);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserializeBody(InputStream stream, TLContext context) throws IOException {
        fileId = readLong(stream);
        filePart = readInt(stream);
        fileTotalParts = readInt(stream);
        bytes = readTLBytes(stream, context);
    }

    @Override
    public String toString() {
        return "upload.saveBigFilePart#de7b673d";
    }

    @Override
    public int getClassId() {
        return CLASS_ID;
    }

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
    }

    public int getFilePart() {
        return filePart;
    }

    public void setFilePart(int filePart) {
        this.filePart = filePart;
    }

    public int getFileTotalParts() {
        return fileTotalParts;
    }

    public void setFileTotalParts(int fileTotalParts) {
        this.fileTotalParts = fileTotalParts;
    }

    public TLBytes getBytes() {
        return bytes;
    }

    public void setBytes(TLBytes bytes) {
        this.bytes = bytes;
    }
}