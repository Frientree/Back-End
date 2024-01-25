package com.d101.frientree.service;

import com.d101.frientree.dto.user.response.UserConfirmationResponse;
import com.d101.frientree.dto.userdto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserConfirmationResponse> getUser(Long id);

    List<UserConfirmationRequestDTO> getAllUsers();

    UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO);

    UserSignInResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO);

    UserTokenRefreshResponseDTO tokenRefresh(UserTokenRefreshRequestDTO userTokenRefreshRequestDTO);

    UserChangeNicknameResponseDTO changeUserNickname(UserChangeNicknameRequestDTO userChangeNicknameRequestDTO);

}
