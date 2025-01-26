package org.romanzhula.telegram_gpt_bot.gpt_openai.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.GptChatHistory;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.Message;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


@Slf4j
@AllArgsConstructor
@Service
public class GptChatHistoryService {

    private final Map<Long, GptChatHistory> chatHistoryMap = new ConcurrentHashMap<>();


    // create history
    public void createChatIdHistory(Long userChatId) {
        chatHistoryMap.putIfAbsent(userChatId, new GptChatHistory(new CopyOnWriteArrayList<>()));
    }

    // get user history
    public Optional<GptChatHistory> getChatIdHistory(Long userChatId) {
        return Optional.ofNullable(chatHistoryMap.get(userChatId));
    }

    // add message to history
    public GptChatHistory updateChatIdHistory(
            Long userChatId,
            Message message
    ) {
        GptChatHistory gptChatHistory = chatHistoryMap.get(userChatId);

        if (gptChatHistory == null) {
            log.error("History not exists. User chatId: {}", userChatId);
            throw new IllegalStateException("History not exists. User chatId: %s".formatted(userChatId));
        }

        gptChatHistory.getChatHistory().add(message);

        return gptChatHistory;
    }

    // clear history
    public void deleteChatIdHistory(Long userChatId) {
        chatHistoryMap.remove(userChatId);
        log.info("Content history with chat GPT cleared. For user chatId: {}", userChatId);
    }

}
