package com.github.badoualy.telegram.sample;

import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.api.UpdateCallback;
import com.github.badoualy.telegram.tl.api.*;
import com.github.badoualy.telegram.tl.api.contacts.TLResolvedPeer;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Random;

import static com.github.badoualy.telegram.sample.C.ApiStorage;
import static com.github.badoualy.telegram.sample.C.application;

/**
 * This snippet will get the most recent conversation and send a message in this conversation.
 */
public class SampleSendMessage {

    public static void main(String[] args) {
        // This is a synchronous client, that will block until the response arrive (or until timeout)
        TelegramClient client = Kotlogram.getDefaultClient(application, new ApiStorage(), Kotlogram.PROD_DC4, new UpdateCallback() {
            @Override
            public void onUpdates(@NotNull final TelegramClient client, @NotNull final TLUpdates updates) {
                System.out.println(updates);
            }

            @Override
            public void onUpdatesCombined(@NotNull final TelegramClient client, @NotNull final TLUpdatesCombined updates) {
                System.out.println(updates);
            }

            @Override
            public void onUpdateShort(@NotNull final TelegramClient client, @NotNull final TLUpdateShort update) {
                System.out.println(update);
            }

            @Override
            public void onShortChatMessage(@NotNull final TelegramClient client, @NotNull final TLUpdateShortChatMessage message) {
                System.out.println(message);
            }

            @Override
            public void onShortMessage(@NotNull final TelegramClient client, @NotNull final TLUpdateShortMessage message) {
                System.out.println(message);
            }

            @Override
            public void onShortSentMessage(@NotNull final TelegramClient client, @NotNull final TLUpdateShortSentMessage message) {
                System.out.println(message);
            }

            @Override
            public void onUpdateTooLong(@NotNull final TelegramClient client) {
                System.out.println(client);
            }
        });

        // You can start making requests

        try {
            TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(0, 0, new TLInputPeerEmpty(), 1);
            final TLResolvedPeer semyonDanilov = client.contactsResolveUsername("SemyonDanilov");
            final TLAbsUser peer = semyonDanilov.getUsers().get(0);
            for (int i = 0; i < 10; i++) {
                final int _i = i;
                new Thread(() -> {
                    TLAbsUpdates tlAbsUpdates = client.messagesSendMessage(new TLInputPeerUser(((TLUser) peer).getId(), ((TLUser) peer).getAccessHash()), "Sent from Kotlogram :)" + _i, Math.abs(new Random().nextLong()));
                }).start();
            }
            // tlAbsUpdates contains the id and date of the message in a TLUpdateShortSentMessage
        } catch (RpcErrorException | IOException e) {
            e.printStackTrace();
        } finally {
            //client.close(); // Important, do not forget this, or your process won't finish
        }
    }

}
