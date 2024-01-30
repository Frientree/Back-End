package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateStatusResponseDTO {

    private Boolean userLeafStatus;
    private Boolean userFruitStatus;

    public static UserCreateStatusResponseDTO createUserCreateStatusResponseDTO(User user) {
        return UserCreateStatusResponseDTO.builder()
                .userFruitStatus(user.getUserFruitStatus())
                .userLeafStatus(user.getUserLeafStatus())
                .build();
    }
}
