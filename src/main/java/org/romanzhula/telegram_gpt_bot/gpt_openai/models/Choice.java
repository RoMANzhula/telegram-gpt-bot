package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Choice(@JsonProperty("message") Message message) {}
