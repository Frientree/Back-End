package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreateStatusResponseDTO {

    @Schema(description = "이파리 생성 가능 여부", example = "true")
    private Boolean userLeafStatus;
    @Schema(description = "열매 생성 가능 여부", example = "true")
    private Boolean userFruitStatus;

    public static UserCreateStatusResponseDTO createUserCreateStatusResponseDTO(User user) {
        return UserCreateStatusResponseDTO.builder()
                .userFruitStatus(user.getUserFruitStatus())
                .userLeafStatus(user.getUserLeafStatus())
                .build();
    }
}
