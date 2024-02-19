package com.d101.frientree.dto.user.response;

import com.d101.frientree.entity.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserListConfirmationResponse {
    @Schema(description = "상태 메시지", example = "Success")
    private String message;
    @Schema(description = "데이터")
    private List<UserInfo> data;

    public static UserListConfirmationResponse createUserListConfirmationResponse(String message, List<User> users) {
        List<UserInfo> userInfoList = users.stream()
                .map(user -> new UserInfo(user.getUserId(), user.getUserEmail(), user.getUserNickname()))
                .collect(Collectors.toList());

        return UserListConfirmationResponse.builder()
                .message(message)
                .data(userInfoList)
                .build();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        @Schema(description = "유저 번호", example = "1")
        private Long userId;
        @Schema(description = "유저 이메일", example = "testuser1@email.com")
        private String userEmail;
        @Schema(description = "닉네임", example = "nickname")
        private String userNickname;
    }
}
