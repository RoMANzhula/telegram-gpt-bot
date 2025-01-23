package org.romanzhula.telegram_gpt_bot.gpt_openai.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.romanzhula.telegram_gpt_bot.gpt_openai.requests.TranslationRequest;
import org.romanzhula.telegram_gpt_bot.gpt_openai.responses.TranscriptionResponse;
import org.springframework.stereotype.Service;

import java.io.File;


@Slf4j
@AllArgsConstructor
@Service
public class GptTranscriberService {

    private final GptClient gptClient;

    public String transcribe(File audioFile) {
        try {
            TranscriptionResponse transcriptionResponse = gptClient.createTranslation(
                    TranslationRequest.builder()
                            .audioFile(audioFile)
                            .model("whisper-1")
                            .build()
            );

            if (transcriptionResponse != null
                    && transcriptionResponse.getText() != null
                    && !transcriptionResponse.getText().isEmpty()
            ) {
                return transcriptionResponse.getText();
            } else {
                log.error("Transcription not completed or answer is empty.");
                throw new IllegalStateException("Transcription not completed or answer is empty.");
            }
        } catch (Exception e) {
            log.error("Error during transcription: {}", e.getMessage());
            throw new RuntimeException("Transcription fails.", e);
        }
    }

}
