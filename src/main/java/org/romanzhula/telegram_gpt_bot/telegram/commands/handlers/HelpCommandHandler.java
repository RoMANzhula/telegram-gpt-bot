package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers;

import org.romanzhula.telegram_gpt_bot.telegram.commands.enums.TelegramCommands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpCommandHandler implements TelegramCommandHandler {

    private final String HELP_MENU_MESSAGE = """
            /help - get all command;
            /start - greeting message with description;
            /clear - command to clear your messages history with GPT;
            """
    ;

    @Override
    public TelegramCommands getSupportedCommand() {
        return TelegramCommands.HELP_COMMAND;
    }

    @Override
    public BotApiMethod<?> processCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(HELP_MENU_MESSAGE)
                .build()
        ;
    }

}
