package org.romanzhula.telegram_gpt_bot.telegram;

import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptService;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptTranscriberService;
import org.romanzhula.telegram_gpt_bot.telegram.async.TelegramAsyncMessageSenderService;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramCommandDispatcher;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramFileService;
import org.romanzhula.telegram_gpt_bot.telegram.components.BotSettings;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotSettings botSettings;
    private final GptService gptService;
    private final TelegramCommandDispatcher telegramCommandDispatcher;
    private final TelegramFileService telegramFileService;
    private final GptTranscriberService gptTranscriberService;
    private final TelegramAsyncMessageSenderService telegramAsyncMessageSenderService;


    public TelegramBot(
            BotSettings botSettings,
            GptService gptService,
            TelegramCommandDispatcher telegramCommandDispatcher,
            @Lazy TelegramFileService telegramFileService,
            GptTranscriberService gptTranscriberService,
            @Lazy TelegramAsyncMessageSenderService telegramAsyncMessageSenderService
    ) {
        super(new DefaultBotOptions(), botSettings.getBotToken());
        this.botSettings = botSettings;
        this.gptService = gptService;
        this.telegramCommandDispatcher = telegramCommandDispatcher;
        this.telegramFileService = telegramFileService;
        this.gptTranscriberService = gptTranscriberService;
        this.telegramAsyncMessageSenderService = telegramAsyncMessageSenderService;
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
        Long userChatId = update.getMessage().getChatId();

        if (telegramCommandDispatcher.isCommand(update)) {
            return List.of(telegramCommandDispatcher.processCommand(update));
        }

        if (update.hasMessage() && update.getMessage().hasVoice()) {
            telegramAsyncMessageSenderService.sendMessageAsync(
                    userChatId.toString(),
                    () -> (SendMessage) processSendVoiceMessage(update.getMessage()),
                    this::getErrorMessageForProcessSendVoiceMessage
            );
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            telegramAsyncMessageSenderService.sendMessageAsync(
                    userChatId.toString(),
                    () -> (SendMessage) processSendTextMessage(update.getMessage()),
                    this::getErrorMessageForProcessSendTextMessage
            );
        }

        return List.of();
    }


    private List<BotApiMethod<?>> processSendVoiceMessage(Message message) {
        Voice voice = message.getVoice();
        String fileId = voice.getFileId();
        Long chatId = message.getChatId();

        File fileVoice = telegramFileService.getFile(fileId);
        String textFromVoiceFile = gptTranscriberService.transcribe(fileVoice);
        String gptTextResponse = gptService.getGptResponse(chatId, textFromVoiceFile);

        SendMessage sendMessage = new SendMessage(chatId.toString(), gptTextResponse);

        return List.of(sendMessage);
    }

    private List<BotApiMethod<?>> processSendTextMessage(Message message) {
        String text = message.getText();
        Long chatId = message.getChatId();

        String gptTextResponse = gptService.getGptResponse(chatId, text);

        SendMessage sendMessage = new SendMessage(chatId.toString(), gptTextResponse);

        return List.of(sendMessage);
    }

    private SendMessage getErrorMessageForProcessSendTextMessage(Throwable throwable) {
        log.error("Error during process send text message! Please try again later.", throwable);

        return SendMessage.builder()
                .text("Error during process send text message! Please try again later.")
                .build()
        ;
    }

    private SendMessage getErrorMessageForProcessSendVoiceMessage(Throwable throwable) {
        log.error("Error during process send voice message! Please try again later.", throwable);

        return SendMessage.builder()
                .text("Error during process send voice message! Please try again later.")
                .build()
        ;
    }

}
