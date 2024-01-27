package com.d101.frientree.service;

import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.UserChangeNicknameResponse;
import com.d101.frientree.dto.user.response.UserConfirmationResponse;
import com.d101.frientree.dto.user.response.UserSignInResponse;
import com.d101.frientree.dto.user.response.UserTokenRefreshResponse;
import com.d101.frientree.dto.user.response.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {

    ResponseEntity<UserConfirmationResponse> confirm(Long id);

    List<UserConfirmationResponseDTO> listConfirm();

    UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO);

    ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest);

    ResponseEntity<UserTokenRefreshResponse> tokenRefresh(UserTokenRefreshRequest userTokenRefreshRequest);

    ResponseEntity<UserChangeNicknameResponse> modifyNickname(UserChangeNicknameRequest userChangeNicknameRequest);

}
