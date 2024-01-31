package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitCreateDTO;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;

@Data
@Builder
public class UserFruitCreateResponse {
    private String message;
    private ArrayList<UserFruitCreateDTO> data;

    public static UserFruitCreateResponse createUserFruitSaveResponse(String message, ArrayList<UserFruitCreateDTO> data){
        return UserFruitCreateResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
