package org.romanzhula.telegram_gpt_bot.gpt_openai;

import org.romanzhula.telegram_gpt_bot.gpt_openai.models.ChatCompletion;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class GptClient {

    private final String token;
    private final RestTemplate restTemplate;

    public GptClient(String token, RestTemplate restTemplate) {
        this.token = token;
        this.restTemplate = restTemplate;
    }


    public ChatCompletion createChatCompletion(String message) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        String requestBody = """
                {
                    "model": "gpt-3.5-turbo",
                    "messages": [
                      {
                        "role": "developer",
                        "content": "You are a helpful assistant."
                      },
                      {
                        "role": "user",
                        "content": "%s"
                      }
                    ],
                    "max_tokens": 100
                  }
                """.formatted(message)
        ;

        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<ChatCompletion> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                ChatCompletion.class
        );

        return responseEntity.getBody();
    }

}
