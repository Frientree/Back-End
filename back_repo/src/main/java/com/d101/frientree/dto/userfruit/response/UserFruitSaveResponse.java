package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitSaveDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserFruitSaveResponse {
    private String message;
    private UserFruitSaveDTO data;

    public static UserFruitSaveResponse createUserFruitSaveResponse(String message, UserFruitSaveDTO data){
        return UserFruitSaveResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
