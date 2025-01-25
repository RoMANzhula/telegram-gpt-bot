package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers;

import lombok.RequiredArgsConstructor;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptChatHistoryService;
import org.romanzhula.telegram_gpt_bot.telegram.commands.enums.TelegramCommands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;


@RequiredArgsConstructor
@Component
public class ClearCommandHandler implements TelegramCommandHandler {

    private final GptChatHistoryService gptChatHistoryService;

    private final String CLEAR_HISTORY_MESSAGE = "Your GPT history has been cleared.";


    @Override
    public TelegramCommands getSupportedCommand() {
        return TelegramCommands.CLEAR_COMMAND;
    }

    @Override
    public BotApiMethod<?> processCommand(Message message) {
        gptChatHistoryService.deleteChatIdHistory(message.getChatId());

        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(CLEAR_HISTORY_MESSAGE)
                .build()
        ;
    }

}
