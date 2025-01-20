package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatCompletion(@JsonProperty("choices") List<Choice> choises) { }
