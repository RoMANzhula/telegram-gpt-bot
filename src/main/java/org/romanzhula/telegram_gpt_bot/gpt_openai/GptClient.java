package org.romanzhula.telegram_gpt_bot.gpt_openai;

import org.romanzhula.telegram_gpt_bot.gpt_openai.models.ChatCompletion;
import org.romanzhula.telegram_gpt_bot.gpt_openai.requests.ChatCompletionRequest;
import org.romanzhula.telegram_gpt_bot.gpt_openai.requests.TranslationRequest;
import org.romanzhula.telegram_gpt_bot.gpt_openai.responses.TranscriptionResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class GptClient {

    private final String token;
    private final RestTemplate restTemplate;

    public GptClient(
            String token,
            RestTemplate restTemplate
    ) {
        this.token = token;
        this.restTemplate = restTemplate;
    }


    public ChatCompletion createChatCompletion(ChatCompletionRequest request) {
        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatCompletionRequest> httpEntity = new HttpEntity<>(request, httpHeaders);

        ResponseEntity<ChatCompletion> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                ChatCompletion.class
        );

        return responseEntity.getBody();
    }

    public TranscriptionResponse createTranslation(
        TranslationRequest translationRequest
    ) {
        String url = "https://api.openai.com/v1/audio/translations";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + token);
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("file", translationRequest.getAudioFile());
        bodyMap.add("model", translationRequest.getModel());

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);

        ResponseEntity<TranscriptionResponse> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                httpEntity,
                TranscriptionResponse.class
        );

        return responseEntity.getBody();
    }

}
