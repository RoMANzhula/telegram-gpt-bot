package org.romanzhula.telegram_gpt_bot.gpt_openai.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class TranslationRequest {

    @JsonProperty("audioFile")
    private File audioFile;

    @JsonProperty("model")
    private String model;

}
