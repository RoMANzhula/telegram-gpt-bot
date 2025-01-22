package org.romanzhula.telegram_gpt_bot.telegram.components;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class BotSettings {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;


    public String getBotToken() {
        return botToken;
    }

    public String getBotName() {
        return botName;
    }
}
