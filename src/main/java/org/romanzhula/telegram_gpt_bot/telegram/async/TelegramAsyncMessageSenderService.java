package org.romanzhula.telegram_gpt_bot.telegram.async;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Service
public class TelegramAsyncMessageSenderService {

    private final DefaultAbsSender defaultAbsSender;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void sendMessageAsync(
            String userChatId,
            Supplier<SendMessage> action,
            Function<Throwable, SendMessage> onErrorHandler
    ) {
        Message initialMessage = sendInitialMessage(userChatId);

        if (initialMessage != null) {
            CompletableFuture.supplyAsync(action, executorService)
                    .exceptionally(onErrorHandler)
                    .thenAccept(sendMessage ->
                            editMessageText(
                                    userChatId,
                                    initialMessage.getMessageId(),
                                    sendMessage.getText()
                            )
                    )
            ;
        }
    }

    private Message sendInitialMessage(String userChatId) {
        try {
            return defaultAbsSender.execute(
                    SendMessage.builder()
                            .text("Loading, please wait.")
                            .chatId(userChatId)
                            .build()
                    )
            ;
        } catch (TelegramApiException e) {
            log.error("Error during initial send message to Telegram.", e);
            throw new RuntimeException(e);
        }
    }

    private void editMessageText(String userChatId, Integer messageId, String newText) {
        try {
            defaultAbsSender.execute(
                    EditMessageText.builder()
                            .chatId(userChatId)
                            .messageId(messageId)
                            .text(newText)
                            .build()
                    )
            ;
        } catch (TelegramApiException e) {
            log.error("Error during editing message text in Telegram.", e);
            throw new RuntimeException(e);
        }
    }
}

