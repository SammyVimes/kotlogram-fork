package ru.sofitlabs.telegram;

import com.github.badoualy.telegram.tl.api.TLAbsInputPeer;
import com.github.badoualy.telegram.tl.api.TLAbsMessageEntity;
import com.github.badoualy.telegram.tl.api.TLAbsReplyMarkup;
import com.github.badoualy.telegram.tl.api.request.TLRequestMessagesSendMessage;
import com.github.badoualy.telegram.tl.core.TLVector;

/**
 * Created by Semyon on 23.02.2017.
 */
public class MessageSender {

    private ProtocolHandler protocolHandler;

    public void sendMessage(final String message) {
        boolean noWebpage = true;
        boolean silent = false;
        boolean background = false;
        boolean clearDraft = false;
        TLAbsInputPeer peer = null;
        Integer replyToMsgId = null;
        long randomId = -1;
        TLAbsReplyMarkup replyMarkup = null;
        TLVector<TLAbsMessageEntity> entities = null;
        final TLRequestMessagesSendMessage messageSendRequest = new TLRequestMessagesSendMessage(noWebpage, silent, background, clearDraft, peer, replyToMsgId, message, randomId, replyMarkup, entities);


        protocolHandler.execute(messageSendRequest);
    }

}
