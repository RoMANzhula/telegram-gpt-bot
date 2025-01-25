package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages;

import lombok.RequiredArgsConstructor;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramCommandDispatcher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TelegramCommandMessageHandler {

    private final TelegramCommandDispatcher telegramCommandDispatcher;


    public boolean isCommand(Message message) {
        return telegramCommandDispatcher.isCommand(message);
    }

    public List<BotApiMethod<?>> handleCommand(Message message) {
        return List.of(telegramCommandDispatcher.processCommand(message));
    }

}
