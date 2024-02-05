package com.d101.frientree.service;

import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserConfirmationResponse> confirm(Long id);

    ResponseEntity<UserListConfirmationResponse> listConfirm();

    ResponseEntity<UserCreateResponse> generateUser(UserCreateRequest userCreateRequest);

    ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest);

    ResponseEntity<UserTokenRefreshResponse> tokenRefreshGenerate(UserTokenRefreshRequest userTokenRefreshRequest);

    ResponseEntity<UserChangeNicknameResponse> modifyNickname(UserChangeNicknameRequest userChangeNicknameRequest);

    ResponseEntity<UserProfileConfirmationResponse> profileConfirm();

    ResponseEntity<UserChangeAlamResponse> modifyAlam(UserChangeAlamRequest userChangeAlamRequest);

    ResponseEntity<UserDeleteResponse> removal();

    ResponseEntity<UserDeactivateResponse> deactivate();

    ResponseEntity<UserDuplicateCheckResponse> duplicateCheck(UserDuplicateCheckRequest userDuplicateCheckRequest);

    ResponseEntity<UserSendEmailCertificationResponse> sendEmailCertificate(UserSendEmailCertificationRequest userSendEmailCertificationRequest);

    ResponseEntity<UserPassEmailCertificationResponse> passEmailCertificate(UserPassEmailCertificationRequest userPassEmailCertificationRequest);

    ResponseEntity<UserChangePasswordResponse> passwordModify(UserChangePasswordRequest userChangePasswordRequest);

    ResponseEntity<UserTemporaryPasswordSendResponse> temporaryPasswordSend(UserTemporaryPasswordSendRequest userTemporaryPasswordSendRequest);

    ResponseEntity<UserCreateStatusResponse> createStatusConfirm();

    void updateAllUserFruitAndLeafStatus();

    ResponseEntity<UserSignInNaverResponse> userSignInNaver(UserSignInNaverRequest userSignInNaverRequest);
}
