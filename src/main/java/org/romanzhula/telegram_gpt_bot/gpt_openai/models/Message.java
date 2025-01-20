package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {

    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;

}
