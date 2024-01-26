package com.d101.frientree.dto.user.response.dto;

import com.d101.frientree.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class UserConfirmationResponseDTO {

    private Long userId;

    private String userNickname;

    private Date userCreateDate;

    private String userEmail;

    public static UserConfirmationResponseDTO createUserConfirmationResponseDTO(User user) {
        return UserConfirmationResponseDTO.builder()
                .userId(user.getUserId())
                .userNickname(user.getUserNickname())
                .userCreateDate(user.getUserCreateDate())
                .userEmail(user.getUserEmail())
                .build();
    }

}
