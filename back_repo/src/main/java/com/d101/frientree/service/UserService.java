package com.d101.frientree.service;

import com.d101.frientree.dto.userdto.*;

import java.util.List;

public interface UserService {

    UserConfirmationRequestDTO getUser(Long id);

    List<UserConfirmationRequestDTO> getAllUsers();

    UserCreateResponseDTO createUser(UserCreateRequestDTO userCreateRequestDTO);

    UserSignInResponseDTO signIn(UserSignInRequestDTO userSignInRequestDTO);

    UserTokenRefreshResponseDTO tokenRefresh(UserTokenRefreshRequestDTO userTokenRefreshRequestDTO);

    UserChangeNicknameResponseDTO changeUserNickname(UserChangeNicknameRequestDTO userChangeNicknameRequestDTO);

}
