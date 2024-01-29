package com.d101.frientree.serviceImpl.userfruit.fastapi;


import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
public class HttpPostAIRequest {
    // URL 설정 (수동 빈 등록 시 값이 등록된다.)
    private String aiUrlString;

    public void sendPostRequest(String sentence) throws Exception {
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
            //System.out.println(response.toString());
            parseAndProcessResponse(response.toString());
        }
    }
    // 응답 파싱 및 처리 메소드
    private static void parseAndProcessResponse(String jsonResponse) {
        Gson gson = new Gson();
        AIResponse response = gson.fromJson(jsonResponse, AIResponse.class);

        List<String> resultList = response.getResult();
        System.out.println("Result: " + resultList);
        // resultList를 기반으로 추가 처리를 수행할 수 있습니다.
    }

    public static void main(String[] args) {
        // HttpPostAIRequest의 인스턴스 생성
        HttpPostAIRequest httpPostAIRequest = new HttpPostAIRequest();
        // 비정적 메소드 호출을 위해 인스턴스 사용
        try {
            httpPostAIRequest.sendPostRequest("string");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
