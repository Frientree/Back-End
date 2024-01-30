package com.d101.frientree.serviceImpl.userfruit.fastapi;


import com.d101.frientree.dto.userfruit.dto.UserFruitSaveDTO;
import com.d101.frientree.dto.userfruit.response.UserFruitSaveResponse;
import com.d101.frientree.entity.fruit.FruitDetail;
import com.d101.frientree.repository.FruitDetailRepository;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.net.HttpURLConnection;
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

    // URL 설정 (수동 빈 등록 시 값이 등록된다.)
    private String aiUrlString;

    public UserFruitSaveResponse sendPostRequest(String sentence) throws Exception {
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
            return parseAndProcessResponse(response.toString());
        }
    }
    // 응답 파싱 및 처리 메소드
    private UserFruitSaveResponse parseAndProcessResponse(String jsonResponse) {
        Gson gson = new Gson();
        AIResponse response = gson.fromJson(jsonResponse, AIResponse.class);

        List<String> resultList = response.getResult();
        //감정 결과 출력
        log.info("Result: {}", resultList);

        ArrayList<UserFruitSaveDTO> fruitDetailList = new ArrayList<>();

        //3가지 감정 List 요소 FruitDetail 정보 가져오기
        for(int i=0;i<3;i++){
            Optional<FruitDetail> fruit = fruitDetailRepository.findByFruitFeel(resultList.get(i));
            if(fruit.isPresent()){
                fruitDetailList.add(UserFruitSaveDTO.createUserFruitSaveDTO(fruit.get()));
            }
        }
        return UserFruitSaveResponse.createUserFruitSaveResponse("Success", fruitDetailList);
    }
}
