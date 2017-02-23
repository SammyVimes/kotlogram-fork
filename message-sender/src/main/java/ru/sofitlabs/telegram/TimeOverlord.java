package ru.sofitlabs.telegram;

import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Semyon on 23.02.2017.
 */
public class TimeOverlord {

    // Delta between server time and client time in ms
    private Map<DataCenter, Long> deltaMap = new HashMap<DataCenter, Long>();

    public TimeOverlord() {
    }

    //ms
    public long getLocalTime() {
        return System.currentTimeMillis();
    }

    public long getServerTime(final DataCenter dataCenter) {
        return getLocalTime() + deltaMap.getOrDefault(dataCenter, 0L);
    }

    // Take time in seconds and shift left
    public long generateMessageId(final DataCenter dataCenter) {
        return this.getServerTime(dataCenter) / (long)1000 << 32;
    }

    public final void setServerTime(@NotNull DataCenter dataCenter, long serverTime) {
        deltaMap.put(dataCenter, serverTime - this.getLocalTime());
//        logger.warn("New server time for " + dataCenter.toString() + " is " + serverTime);
//        logger.warn("New time delta for " + dataCenter.toString() + " is " + (Long)deltaMap.get(dataCenter));
    }

    // Reverse operation, shift right then multiply by 1000
    public final void synchronizeTime(@NotNull DataCenter dataCenter, long messageId) {
        this.setServerTime(dataCenter, (messageId >>> 32) * (long)1000);
    }

}
