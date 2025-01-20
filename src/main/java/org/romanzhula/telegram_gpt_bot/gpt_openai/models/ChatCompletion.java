package org.romanzhula.telegram_gpt_bot.gpt_openai.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatCompletion {

    @JsonProperty("choices")
    private List<Choice> choises;

}
