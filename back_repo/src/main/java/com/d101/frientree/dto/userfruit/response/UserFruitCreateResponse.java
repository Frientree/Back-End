package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitCreateDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserFruitCreateResponse {
    private String message;
    private List<UserFruitCreateDTO> data;

    public static UserFruitCreateResponse createUserFruitSaveResponse(String message, List<UserFruitCreateDTO> data){
        return UserFruitCreateResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
