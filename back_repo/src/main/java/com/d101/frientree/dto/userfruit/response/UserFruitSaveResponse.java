package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitSaveDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class UserFruitSaveResponse {
    private String message;
    private ArrayList<UserFruitSaveDTO> data;

    public static UserFruitSaveResponse createUserFruitSaveResponse(String message, ArrayList<UserFruitSaveDTO> data){
        return UserFruitSaveResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
