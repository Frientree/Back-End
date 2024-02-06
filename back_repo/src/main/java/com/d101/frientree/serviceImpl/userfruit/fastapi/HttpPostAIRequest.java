package com.d101.frientree.serviceImpl.userfruit.fastapi;


import com.d101.frientree.dto.userfruit.dto.UserFruitCreateDTO;
import com.d101.frientree.dto.userfruit.response.UserFruitCreateResponse;
import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.repository.FruitDetailRepository;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.service.mongo.MongoEmotionService;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Log4j2
public class HttpPostAIRequest {
    @Autowired
    private FruitDetailRepository fruitDetailRepository;
    @Autowired
    private MongoEmotionService mongoEmotionService;
    @Autowired
    private UserRepository userRepository;

    // URL 설정 (수동 빈 등록 시 값이 등록된다.)
    private String aiUrlString;
    private String aiS3UrlString;

    public UserFruitCreateResponse sendPostRequest(String sentence) throws Exception {
        URL url = new URL(aiUrlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // HTTP 메소드 및 헤더 설정
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // JSON 본문 구성
        String jsonInputString = "{\"sentence\":\"" + sentence + "\"}";

        // 요청 본문에 데이터 작성
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        // 응답 받기
        try(java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return parseAndProcessResponse(response.toString(), sentence);
        }
    }
    // 응답 파싱 및 처리 메소드
    private UserFruitCreateResponse parseAndProcessResponse(String jsonResponse, String sentence) {
        Gson gson = new Gson();
        AIResponse response = gson.fromJson(jsonResponse, AIResponse.class);

        List<String> resultList = response.getResult();
        //감정 결과 출력
        log.info("Result: {}", resultList);

        //사용자 정보 가져오기 (PK 값)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //감정 1순위 결과 NoSQL 저장 (text : sentence)
        mongoEmotionService.createEmotion(authentication.getName(), sentence, resultList.get(0));

        List<UserFruitCreateDTO> fruitDetailList = new ArrayList<>();

        //3가지 감정 List 요소 FruitDetail 정보 가져오기
        for(int i=0;i<3;i++){
            Optional<FruitDetail> fruit = fruitDetailRepository.findByFruitFeel(resultList.get(i));
            if(fruit.isPresent()){
                fruitDetailList.add(UserFruitCreateDTO.createUserFruitCreateDTO(fruit.get()));
            }
        }
        return UserFruitCreateResponse.createUserFruitSaveResponse("Success", fruitDetailList);
    }

    public void csvFileS3UploadUrlSend(String sendFileS3Url) throws Exception {
        URL url = new URL(aiS3UrlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // HTTP 메소드 및 헤더 설정
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // JSON 본문 구성
        String jsonInputString = "{\"s3Url\":\"" + sendFileS3Url + "\"}";

        System.out.println(aiS3UrlString);
        System.out.println(url);
        System.out.println(jsonInputString);

        // 요청 본문에 데이터 작성
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        System.out.println("본문 데이터 작성 함");

        // 서버로부터의 응답 읽기
        try(InputStream is = connection.getInputStream()) {
            System.out.println("try in");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line.trim());
            }
            System.out.println("Response from the server: " + response.toString());
        } catch (IOException e) {
            // 서버로부터 오류 응답 처리
            InputStream errorStream = connection.getErrorStream();
            if (errorStream != null) {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(errorStream))) {
                    String line;
                    StringBuilder errorResponse = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.out.println("Error response from the server: " + errorResponse.toString());
                }
            }
            // 예외를 다시 던지거나 적절히 처리
            throw e;
        }
    }
}
