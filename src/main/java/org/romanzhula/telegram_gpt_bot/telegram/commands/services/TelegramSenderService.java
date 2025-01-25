package org.romanzhula.telegram_gpt_bot.telegram.commands.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramSenderService {

    private final DefaultAbsSender defaultAbsSender;

    public Message sendInitialMessage(String userChatId) {
        try {
            return defaultAbsSender.execute(
                    SendMessage.builder()
                            .text("Loading, please wait.")
                            .chatId(userChatId)
                            .build()
            );
        } catch (TelegramApiException e) {
            log.error("Error during initial send message to Telegram.", e);
            throw new RuntimeException(e);
        }
    }

    public void editMessageText(String userChatId, Integer messageId, String newText) {
        try {
            defaultAbsSender.execute(
                    EditMessageText.builder()
                            .chatId(userChatId)
                            .messageId(messageId)
                            .text(newText)
                            .build()
            );
        } catch (TelegramApiException e) {
            log.error("Error during editing message text in Telegram.", e);
            throw new RuntimeException(e);
        }
    }

}
