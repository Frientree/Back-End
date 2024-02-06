package com.d101.frientree.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void initialize() {
        try { //Firebase 설정 파일 로드
            ClassPathResource resource = new ClassPathResource("firebase-service-account.json"); //resources 밑에 있는 json 파일 들고옴
            GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());

            //필요한 추가 설정이 있다면 builder 밑에 추가
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();

            //FirebaseApp 초기화
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
