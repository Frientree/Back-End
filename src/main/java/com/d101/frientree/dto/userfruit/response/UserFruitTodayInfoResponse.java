package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitTodayInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitTodayInfoResponse {
    @Schema(description = "상태 코드", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private UserFruitTodayInfoDTO data;

    public static UserFruitTodayInfoResponse createUserFruitTodayInfoResponse(String message, UserFruitTodayInfoDTO data){
        return UserFruitTodayInfoResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
