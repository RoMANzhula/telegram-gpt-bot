package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.concurrent.CopyOnWriteArrayList;

// TODO: update to Entity for store to DB

@Data
@Builder
@AllArgsConstructor
public class GptChatHistory {

    @JsonProperty("chatHistory")
    private CopyOnWriteArrayList<Message> chatHistory;

}
