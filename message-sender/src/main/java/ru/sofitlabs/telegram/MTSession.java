package ru.sofitlabs.telegram;

import com.github.badoualy.telegram.tl.TLObjectUtils;
import com.github.badoualy.telegram.tl.core.TLObject;

import java.math.BigInteger;

/**
 * Created by Semyon on 23.02.2017.
 */
public class MTSession {

    private DataCenter dataCenter;

    private byte[] id = RandomUtils.randomSessionId();

    private Long salt = 0L;

    private int contentRelatedCount = 0;

    private Long lastMessageId = 0L;

    private transient String tag;

    private TimeOverlord timeOverlord;

    public MTSession(final TimeOverlord timeOverlord, final DataCenter dataCenter, final byte[] id, final Long salt, final int contentRelatedCount, final Long lastMessageId, final String tag) {
        this.dataCenter = dataCenter;
        this.id = id;
        this.salt = salt;
        this.contentRelatedCount = contentRelatedCount;
        this.lastMessageId = lastMessageId;
        this.tag = tag + ":" + new BigInteger(id).longValue();
        this.timeOverlord = timeOverlord;
    }

    /**
     * Generate a valid seqNo value for the given message type
     *
     * @param clazz message type
     * @return a valid seqNo value to send
     * @see <a href="https://core.telegram.org/mtproto/description#message-sequence-number-msg-seqno">MTProto description</a>
     */
    private int generateSeqNo(final Class<? extends TLObject> clazz) {
        return generateSeqNo(TLObjectUtils.isContentRelated(clazz));
    }

    private int generateSeqNo(final boolean contentRelated) {
        int seqNo = -1;
        synchronized (this) {
            if (contentRelated) {
                seqNo = contentRelatedCount * 2 + 1;
                contentRelatedCount++;
            } else {
                seqNo = contentRelatedCount * 2;
            }
        }
        return seqNo;
    }

    public int generateSeqNo(final TLObject message) {
        return generateSeqNo(message.getClass());
    }


    public long generateMessageId() {
        long weakMessageId = timeOverlord.generateMessageId(dataCenter);
        synchronized (this) {
            lastMessageId = Math.max(weakMessageId, lastMessageId + 4);
        }
        return lastMessageId;
    }
}
