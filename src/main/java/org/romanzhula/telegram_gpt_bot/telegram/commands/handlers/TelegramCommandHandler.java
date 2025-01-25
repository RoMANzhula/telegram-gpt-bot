package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers;

import org.romanzhula.telegram_gpt_bot.telegram.commands.enums.TelegramCommands;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface TelegramCommandHandler {

    TelegramCommands getSupportedCommand();

    BotApiMethod<?> processCommand(Message message);

}
