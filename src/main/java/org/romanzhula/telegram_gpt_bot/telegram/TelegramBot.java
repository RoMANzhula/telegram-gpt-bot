package org.romanzhula.telegram_gpt_bot.telegram;

import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptService;
import org.romanzhula.telegram_gpt_bot.telegram.commands.TelegramCommandDispatcher;
import org.romanzhula.telegram_gpt_bot.telegram.components.BotSettings;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;


@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotSettings botSettings;
    private final GptService gptService;
    private final TelegramCommandDispatcher telegramCommandDispatcher;

    public TelegramBot(
            BotSettings botSettings,
            GptService gptService,
            TelegramCommandDispatcher telegramCommandDispatcher
    ) {
        super(new DefaultBotOptions(), botSettings.getBotToken());
        this.botSettings = botSettings;
        this.gptService = gptService;
        this.telegramCommandDispatcher = telegramCommandDispatcher;
    }

    @Override
    public String getBotUsername() {
        return botSettings.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        try {
            List<BotApiMethod<?>> botApiMethods = processUpdate(update);

            botApiMethods.forEach(method -> {
                try {
                    sendApiMethod(method);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            System.out.println("Error from onUpdateReceived() method.");
            sendMessageErrorToChat(update.getMessage().getChatId());
            throw new RuntimeException(e);
        }
    }

    private void sendMessageErrorToChat(Long userChatId) {
        try {
            sendApiMethod(SendMessage.builder()
                    .chatId(userChatId)
                    .text("Error! Try again later.")
                    .build()
            );
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private List<BotApiMethod<?>> processUpdate(Update update) {

        if (telegramCommandDispatcher.isCommand(update)) {
            return List.of(telegramCommandDispatcher.processCommand(update));
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            String gptTextResponse = gptService.getGptResponse(chatId, text);

            SendMessage sendMessage = new SendMessage(chatId.toString(), gptTextResponse);

            return List.of(sendMessage);
        }

        return List.of();
    }

}
