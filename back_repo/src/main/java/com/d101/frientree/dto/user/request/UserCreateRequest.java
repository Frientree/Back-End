package com.d101.frientree.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String userEmail;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)[^/'\"@-]{8,16}$", message = "비밀번호는 숫자, 영문, 특수문자(/, ', \", @, - 제외)를 포함한 8~16자 이내여야 합니다.")
    private String userPw;

    @Size(min = 1, max = 8, message = "닉네임은 1~8자 이내여야 합니다.")
    private String userNickname;

}
