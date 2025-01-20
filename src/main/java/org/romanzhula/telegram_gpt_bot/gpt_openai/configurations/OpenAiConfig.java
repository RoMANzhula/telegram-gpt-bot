package org.romanzhula.telegram_gpt_bot.gpt_openai.configurations;

import org.romanzhula.telegram_gpt_bot.gpt_openai.GptClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    public GptClient gptClient(
            @Value("${openai.token}") String gptToken,
            RestTemplateBuilder restTemplateBuilder
    ) {
        return new GptClient(gptToken, restTemplateBuilder.build());
    }

}
