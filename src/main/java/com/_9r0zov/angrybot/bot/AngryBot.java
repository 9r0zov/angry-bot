package com._9r0zov.angrybot.bot;

import com._9r0zov.angrybot.service.ResponseMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Component
@Log4j2
@RequiredArgsConstructor
public class AngryBot extends TelegramLongPollingBot {

    private static final Random RND = new Random();

    @Value("${bot.username}")
    private String username;

    @Value("${bot.token}")
    private String token;

    @Value("${bot.responsePercent:0.66}")
    private Double responsePercent;

    private final ResponseMessageService response;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && RND.nextDouble() < responsePercent) {
            getSendMessage(update)
                    .ifPresent(message -> {
                        try {
                            log.info("Responding to message {{}} with response {{}}",
                                    update.getMessage(), message);
                            execute(message);
                        } catch (TelegramApiException e) {
                            log.error("Can't execute message", e);
                        }
                    });
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    private Optional<SendMessage> getSendMessage(Update update) {
        Optional<SendMessage> msg;
        if (update.getMessage().hasPhoto()) {
            msg = response.getPhotoMessageReply(update);
        } else if (update.getMessage().hasVoice()) {
            msg = response.getAudioMessageReply(update);
        } else {
            msg = response.getAngryMessageReply(update);
        }
        return msg;
    }
}
