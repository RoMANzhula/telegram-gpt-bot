package org.romanzhula.telegram_gpt_bot.gpt_openai.services;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.ChatCompletion;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.Message;
import org.romanzhula.telegram_gpt_bot.gpt_openai.requests.ChatCompletionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GptService {

    private final GptClient gptClient;

    @Nonnull
    public String getGptResponse(
        Long userId,
        String userText
    ) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        Message.builder()
                                .content(userText)
                                .role("user")
                                .build()
                ))
                .maxTokens(100) // set limit for tokens
                .build()
        ;

        ChatCompletion response = gptClient.createChatCompletion(request);

        return response.getChoises().getFirst().getMessage().getContent();
    }

}
