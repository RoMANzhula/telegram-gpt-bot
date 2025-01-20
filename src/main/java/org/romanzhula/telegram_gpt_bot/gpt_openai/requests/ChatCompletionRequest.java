package org.romanzhula.telegram_gpt_bot.gpt_openai.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.romanzhula.telegram_gpt_bot.gpt_openai.models.Message;

import java.util.List;

@Data
@Builder
public class ChatCompletionRequest {

        @JsonProperty("model")
        private String model;

        @JsonProperty("messages")
        private List<Message> messages;

        @JsonProperty("max_tokens")
        private int maxTokens;

}
