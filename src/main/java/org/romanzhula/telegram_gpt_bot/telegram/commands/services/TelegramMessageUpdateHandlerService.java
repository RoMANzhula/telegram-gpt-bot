package org.romanzhula.telegram_gpt_bot.telegram.commands.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.telegram.async.TelegramAsyncMessageSenderService;
import org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages.TelegramCommandMessageHandler;
import org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages.TelegramTextMessageHandler;
import org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages.TelegramVoiceMessageHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class TelegramMessageUpdateHandlerService {

    private final TelegramCommandMessageHandler commandMessageHandler;
    private final TelegramTextMessageHandler textMessageHandler;
    private final TelegramVoiceMessageHandler voiceMessageHandler;
    private final TelegramAsyncMessageSenderService asyncMessageSender;


    public List<BotApiMethod<?>> handleUpdate(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            Long userChatId = message.getChatId();

            if (commandMessageHandler.isCommand(message)) {
                return commandMessageHandler.handleCommand(message);
            }

            if (message.hasVoice()) {
                asyncMessageSender.sendMessageAsync(
                        userChatId.toString(),
                        () -> (SendMessage) voiceMessageHandler.handleVoiceMessage(message),
                        throwable -> {
                            log.error("Error processing voice message for user: {}", userChatId, throwable);
                            return new SendMessage(
                                    userChatId.toString(),
                                    "Error processing voice message."
                            );
                        }
                );
            }

            if (message.hasText()) {
                asyncMessageSender.sendMessageAsync(
                        userChatId.toString(),
                        () -> (SendMessage) textMessageHandler.handleTextMessage(message),
                        throwable -> {
                            log.error("Error processing text message for user: {}", userChatId, throwable);
                            return new SendMessage(
                                    userChatId.toString(),
                                    "Error processing text message."
                            );
                        }
                );
            }
        }

        return List.of();
    }

}
