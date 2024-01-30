package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.user.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserProfileConfirmationResponseDTO {

    private Long userId;

    private String userNickname;

    private boolean userLeafStatus;

    private String userEmail;

    private boolean userFruitStatus;

    public static UserProfileConfirmationResponseDTO createUserProfileConfirmationResponseDTO(User user) {
        return UserProfileConfirmationResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userLeafStatus(user.getUserLeafStatus())
                .userFruitStatus(user.getUserFruitStatus())
                .userEmail(user.getUserEmail())
                .build();
    }
}
