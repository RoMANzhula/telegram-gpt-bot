package org.romanzhula.telegram_gpt_bot.telegram.async;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramSenderService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Service
public class TelegramAsyncMessageSenderService {

    private final ObjectProvider<TelegramSenderService> telegramSenderServiceProvider;
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void sendMessageAsync(
            String userChatId,
            Supplier<SendMessage> action,
            Function<Throwable, SendMessage> onErrorHandler
    ) {
        TelegramSenderService telegramSenderService = telegramSenderServiceProvider.getIfAvailable();

        if (telegramSenderService == null) {
            log.error("TelegramSenderService is not available");
            return;
        }

        Message initialMessage = telegramSenderService.sendInitialMessage(userChatId);

        if (initialMessage != null) {
            CompletableFuture.supplyAsync(action, executorService)
                    .exceptionally(onErrorHandler)
                    .thenAccept(sendMessage ->
                            telegramSenderService.editMessageText(
                                    userChatId,
                                    initialMessage.getMessageId(),
                                    sendMessage.getText()
                            )
                    )
            ;
        }
    }

}

