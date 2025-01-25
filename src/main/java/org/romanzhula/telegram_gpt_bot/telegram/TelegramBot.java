package org.romanzhula.telegram_gpt_bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramMessageUpdateHandlerService;
import org.romanzhula.telegram_gpt_bot.telegram.components.BotSettings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotSettings botSettings;
    private final TelegramMessageUpdateHandlerService messageHandlerService;

    public TelegramBot(
            BotSettings botSettings,
            TelegramMessageUpdateHandlerService messageHandlerService
    ) {
        super(new DefaultBotOptions(), botSettings.getBotToken());
        this.botSettings = botSettings;
        this.messageHandlerService = messageHandlerService;
    }

    @Override
    public String getBotUsername() {
        return botSettings.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            List<BotApiMethod<?>> responses = messageHandlerService.handleUpdate(update);
            responses.forEach(response -> {
                try {
                    sendApiMethod(response);
                } catch (TelegramApiException e) {
                    log.error("Error sending response: ", e);
                }
            });
        } catch (Exception e) {
            log.error("Error processing update: ", e);
        }
    }
}
