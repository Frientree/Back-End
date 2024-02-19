package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitSaveDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitSaveResponse {
    @Schema(description = "상태 코드", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserFruitSaveDTO data;

    public static UserFruitSaveResponse createUserFruitSaveResponse(String message, UserFruitSaveDTO data){
        return UserFruitSaveResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
