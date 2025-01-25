package org.romanzhula.telegram_gpt_bot.telegram.commands.services;

import lombok.AllArgsConstructor;
import org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.TelegramCommandHandler;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class TelegramCommandDispatcher {

    private final List<TelegramCommandHandler> commandHandlerList;

    public BotApiMethod<?> processCommand(Message message) {
        String text = message.getText();

        if (!text.startsWith("/")) {
            throw new IllegalArgumentException("Not valid command: %s. Will add /".formatted(text));
        }

        if (!isCommand(message)) {
            throw new IllegalArgumentException("It's not a command: %s. Try again please.".formatted(text));
        }

        Optional<TelegramCommandHandler> suitedCommandHandler = commandHandlerList.stream()
                .filter(command -> command.getSupportedCommand().getValue().equals(text))
                .findAny()
        ;

        if (suitedCommandHandler.isEmpty()) {
            System.out.printf("Such a command does not exist. Check your command: %s.%n", text);

            return SendMessage.builder()
                    .chatId(message.getChatId())
                    .text("Such a command does not exist. Check your command: %s.%n".formatted(text))
                    .build()
            ;
        }

        return suitedCommandHandler.orElseThrow().processCommand(message);
    }

    public boolean isCommand(Message message) {
        return message.hasText() && message.getText().startsWith("/");
    }

}
