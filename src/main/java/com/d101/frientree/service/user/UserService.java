package com.d101.frientree.service.user;

import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<UserConfirmationResponse> confirm(Long id);

    ResponseEntity<UserListConfirmationResponse> listConfirm();

    ResponseEntity<UserGenerationResponse> userGenerate(UserGenerationRequest userGenerationRequest);

    ResponseEntity<UserSignInResponse> signIn(UserSignInRequest userSignInRequest);

    ResponseEntity<UserTokenRefreshGenerationResponse> tokenRefreshGenerate(UserTokenRefreshGenerationRequest userTokenRefreshGenerationRequest);

    ResponseEntity<UserNicknameModificationResponse> nicknameModify(UserNicknameModificationRequest userNicknameModificationRequest);

    ResponseEntity<UserProfileConfirmationResponse> profileConfirm();

    ResponseEntity<UserAlamModificationResponse> alamModify(UserAlamModificationRequest userAlamModificationRequest);

    ResponseEntity<UserRemovalResponse> remove();

    ResponseEntity<UserDeactivationResponse> deactivate();

    ResponseEntity<UserDuplicateCheckResponse> duplicateCheck(UserDuplicateCheckRequest userDuplicateCheckRequest);

    ResponseEntity<UserSendEmailCertificationResponse> sendEmailCertificate(UserSendEmailCertificationRequest userSendEmailCertificationRequest);

    ResponseEntity<UserPassEmailCertificationResponse> passEmailCertificate(UserPassEmailCertificationRequest userPassEmailCertificationRequest);

    ResponseEntity<UserPasswordModificationResponse> passwordModify(UserPasswordModificationRequest userPasswordModificationRequest);

    ResponseEntity<UserTemporaryPasswordSendMailResponse> temporaryPasswordSend(UserTemporaryPasswordSendMailRequest userTemporaryPasswordSendMailRequest);

    ResponseEntity<UserCreateStatusConfirmationResponse> createStatusConfirm();

    void updateAllUserFruitAndLeafStatus();

    ResponseEntity<UserSignInNaverResponse> userSignInNaver(UserSignInNaverRequest userSignInNaverRequest);

    ResponseEntity<UserFcmTokenUpdateResponse> updateFcmToken(UserFcmTokenUpdateRequest request);
}
