package org.romanzhula.telegram_gpt_bot.telegram.helpers;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Slf4j
public class ErrorMessageBuilder {

    public static SendMessage buildErrorMessage(
            String chatId,
            String errorMessage,
            Throwable throwable
    ) {
        log.error(errorMessage, throwable);
        return SendMessage.builder()
                .chatId(chatId)
                .text(errorMessage)
                .build()
        ;
    }

}
