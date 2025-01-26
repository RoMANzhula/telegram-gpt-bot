package org.romanzhula.telegram_gpt_bot.telegram.commands.handlers.messages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptService;
import org.romanzhula.telegram_gpt_bot.gpt_openai.services.GptTranscriberService;
import org.romanzhula.telegram_gpt_bot.telegram.commands.services.TelegramFileService;
import org.romanzhula.telegram_gpt_bot.telegram.helpers.ErrorMessageBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.File;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Component
public class TelegramVoiceMessageHandler {

    private final ObjectProvider<TelegramFileService> telegramFileServiceProvider;
    private final GptTranscriberService gptTranscriberService;
    private final GptService gptService;


    public List<BotApiMethod<?>> handleVoiceMessage(Message message) {
        TelegramFileService telegramFileService = telegramFileServiceProvider.getIfAvailable();

        Long chatId = message.getChatId();
        Voice voice = message.getVoice();

        try {
            File voiceFile = null;
            if (telegramFileService != null) {
                voiceFile = telegramFileService.getFile(voice.getFileId());
            }

            String transcribedText = gptTranscriberService.transcribe(voiceFile);
            String response = gptService.getGptResponse(chatId, transcribedText);
            return List.of(new SendMessage(chatId.toString(), response));
        } catch (Exception exception) {
            log.error("Error during processing voice message!", exception);
            return List.of(
                    ErrorMessageBuilder.buildErrorMessage(
                            chatId.toString(),
                            "Error during processing voice message! Please try again later.",
                            exception
                    )
            );
        }
    }

}
