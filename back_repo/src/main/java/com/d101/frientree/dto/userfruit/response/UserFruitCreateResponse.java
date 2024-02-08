package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserFruitCreateResponse {
    @Schema(description = "상태 메시지", nullable = false, example = "Success")
    private String message;
    @Schema(description = "데이터", nullable = false)
    private List<UserFruitCreateDTO> data;

    public static UserFruitCreateResponse createUserFruitSaveResponse(String message, List<UserFruitCreateDTO> data){
        return UserFruitCreateResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
