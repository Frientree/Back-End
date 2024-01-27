package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserProfileConfirmationResponseDTO {

    private Long userId;

    private String userNickname;

    private boolean userDisabled;

    private Date userCreateDate;

    private boolean userLeafStatus;

    private String userEmail;

    private boolean userNotification;

    private boolean userFruitStatus;

    public static UserProfileConfirmationResponseDTO createUserProfileConfirmationResponseDTO(User user) {
        return UserProfileConfirmationResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userDisabled(user.getUserDisabled())
                .userLeafStatus(user.getUserLeafStatus())
                .userNotification(user.getUserNotification())
                .userFruitStatus(user.getUserFruitStatus())
                .userCreateDate(user.getUserCreateDate())
                .userEmail(user.getUserEmail())
                .build();
    }
}
