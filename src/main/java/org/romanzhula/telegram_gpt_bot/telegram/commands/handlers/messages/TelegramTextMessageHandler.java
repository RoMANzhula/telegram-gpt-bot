package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptService;
import org.romanzhula.telegram_gpt_bot.telegram.helpers.ErrorMessageBuilder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramTextMessageHandler {

    private final GptService gptService;


    public List<BotApiMethod<?>> handleTextMessage(Message message) {
        Long chatId = message.getChatId();
        String text = message.getText();

        try {
            String response = gptService.getGptResponse(chatId, text);
            return List.of(new SendMessage(chatId.toString(), response));
        } catch (Exception exception) {
            return List.of(
                    ErrorMessageBuilder.buildErrorMessage(
                            chatId.toString(),
                            "Error during processing text message! Please try again later.",
                            exception
                    )
            );
        }
    }

}
