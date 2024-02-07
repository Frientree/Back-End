package com.d101.frientree.dto.userfruit.response;

import com.d101.frientree.dto.userfruit.dto.UserFruitTodayInfoDTO;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFruitTodayInfoResponse {
    private String message;
    private UserFruitTodayInfoDTO data;

    public static UserFruitTodayInfoResponse createUserFruitTodayInfoResponse(String message, UserFruitTodayInfoDTO data){
        return UserFruitTodayInfoResponse.builder()
                .message(message)
                .data(data)
                .build();
    }
}
