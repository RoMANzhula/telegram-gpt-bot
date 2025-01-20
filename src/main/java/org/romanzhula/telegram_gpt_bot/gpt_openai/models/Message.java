package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public record Message(
        @JsonProperty("role") String role,
        @JsonProperty("content") String content
) {}
