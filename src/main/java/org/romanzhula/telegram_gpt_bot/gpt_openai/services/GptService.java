package org.romanzhula.telegram_gpt_bot.gpt_openai.services;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.ChatCompletion;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.GptChatHistory;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.Message;
import org.romanzhula.telegram_gpt_bot.gpt_openai.requests.ChatCompletionRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class GptService {

    private final GptClient gptClient;
    private final GptChatHistoryService gptChatHistoryService;

    @Nonnull
    public String getGptResponse(
        Long userChatId,
        String userText
    ) {
        gptChatHistoryService.createChatIdHistory(userChatId);

        GptChatHistory gptChatHistory = gptChatHistoryService.updateChatIdHistory(
                userChatId,
                Message.builder()
                        .content(userText)
                        .role("user")
                        .build()
        );

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(gptChatHistory.getChatHistory())
                .maxTokens(100) // set limit for tokens
                .build()
        ;

        ChatCompletion response = gptClient.createChatCompletion(request);
        Message messageFromGpt = response.getChoises().getFirst().getMessage();
        gptChatHistoryService.updateChatIdHistory(userChatId, messageFromGpt);

        return response.getChoises().getFirst().getMessage().getContent();
    }

}
