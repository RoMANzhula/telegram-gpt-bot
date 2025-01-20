package org.romanzhula.telegram_gpt_bot;

import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.ChatCompletion;
import org.romanzhula.telegram_gpt_bot.telegram.BotSettings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotSettings botSettings;
    private final GptClient gptClient;

    public TelegramBot(
            DefaultBotOptions options,
            BotSettings botSettings,
            GptClient gptClient
    ) {
        super(options, botSettings.getBotToken());
        this.botSettings = botSettings;
        this.gptClient = gptClient;
    }

    @Override
    public String getBotUsername() {
        return botSettings.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            ChatCompletion chatCompletionResponse = gptClient.createChatCompletion(text);
            String textResponse = chatCompletionResponse.choises().get(0).message().content();

            SendMessage sendMessage = new SendMessage(chatId.toString(), textResponse);

            try {
                sendApiMethod(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
