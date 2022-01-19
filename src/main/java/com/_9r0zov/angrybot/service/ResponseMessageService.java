package com._9r0zov.angrybot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ResponseMessageService {

    private static final Random RND = new Random();

    private final MessageService msg;

    public Optional<SendMessage> getAngryMessageReply(Update update) {
        if (update.getMessage().getText().contains("?")) {
            String text = msg.get("angry.0");
            return Optional.of(getMessageWithReply(update, text));
        }
        return Optional.empty();
    }

    public Optional<SendMessage> getPhotoMessageReply(Update update) {
        int audioNum = RND.nextInt(3);
        String text = msg.get("img." + audioNum);
        return Optional.of(getMessageWithReply(update, text));
    }

    public Optional<SendMessage> getAudioMessageReply(Update update) {
        String text = msg.get("audio.0");
        return Optional.of(getMessageWithReply(update, text));
    }

    private SendMessage getMessageWithReply(Update update, String text) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .replyToMessageId(update.getMessage().getMessageId())
                .text(text)
                .build();
    }
}
