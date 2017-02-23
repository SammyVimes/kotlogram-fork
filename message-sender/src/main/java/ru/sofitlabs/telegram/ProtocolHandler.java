package ru.sofitlabs.telegram;

import com.github.badoualy.telegram.tl.api.request.TLRequestMessagesSendMessage;

import java.io.IOException;

/**
 * Created by Semyon on 23.02.2017.
 */
public class ProtocolHandler {

    private MTSession session;



    public void execute(final TLRequestMessagesSendMessage messageSendRequest) throws IOException {
        new MTMessage(session.generateMessageId(), session.generateSeqNo(messageSendRequest), messageSendRequest.serialize());
    }

}
