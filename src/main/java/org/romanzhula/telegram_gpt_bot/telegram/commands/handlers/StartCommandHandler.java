package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers;

import org.romanzhula.telegram_gpt_bot.telegram.commands.enums.TelegramCommands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@Component
public class StartCommandHandler implements TelegramCommandHandler {

    private final String GREETING_MESSAGE = """
            Welcome %s!
            This Bot is for your Chat GPT.
            Write your question and you'll receive an answer within a few seconds.
            Watch all command you can via command '/help'
            """
    ;

    @Override
    public TelegramCommands getSupportedCommand() {
        return TelegramCommands.START_COMMAND;
    }

    @Override
    public BotApiMethod<?> processCommand(Message message) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(GREETING_MESSAGE.formatted(message.getChat().getFirstName()))
                .build()
        ;
    }

}
