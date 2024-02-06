package com.d101.frientree.config;

import com.d101.frientree.serviceImpl.userfruit.clova.ClovaSpeechClient;
import com.d101.frientree.serviceImpl.userfruit.fastapi.AIResponse;
import com.d101.frientree.serviceImpl.userfruit.fastapi.HttpPostAIRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class SecretKeyConfig {
    @Value("${jasypt.encryptor.naver.clova.key}")
    private String secret;

    @Value("${jasypt.encryptor.naver.clova.invokeurl}")
    private String invokeUrl;

    @Value("${jasypt.encryptor.aiurl}")
    private String aiUrl;

    @Value("${jasypt.encryptor.ais3url}")
    private String aiS3Url;

    @Bean
    public ClovaSpeechClient clovaSpeechClient() {
        ClovaSpeechClient client = new ClovaSpeechClient();
        client.setSECRET(secret);
        client.setINVOKE_URL(invokeUrl);
        return client;
    }

    @Bean
    public HttpPostAIRequest aiResponse(){
        HttpPostAIRequest httpPostAIRequest = new HttpPostAIRequest();
        httpPostAIRequest.setAiUrlString(aiUrl);
        httpPostAIRequest.setAiS3UrlString(aiS3Url);
        return httpPostAIRequest;
    }
}
