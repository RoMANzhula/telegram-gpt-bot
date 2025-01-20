package org.romanzhula.telegram_gpt_bot.configurations;

import org.romanzhula.telegram_gpt_bot.TelegramBot;
import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.romanzhula.telegram_gpt_bot.telegram.BotSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi() {
        try {
            return new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TelegramBot telegramBot(
        BotSettings botSettings,
        TelegramBotsApi telegramBotsApi,
        GptClient gptClient
    ) {
        DefaultBotOptions defaultBotOptions = new DefaultBotOptions();
        TelegramBot telegramBot = new TelegramBot(defaultBotOptions, botSettings, gptClient);

        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

        return telegramBot;
    }

}
