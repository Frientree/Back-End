package com.d101.frientree.controller;

import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import com.d101.frientree.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Log4j2
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    // 유저 개별 조회
    @Operation(summary = "단일 유저 조회", description = "개별 유저를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserConfirmationResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserConfirmationResponse> userConfirmation(@PathVariable Long userId) {
        return userService.confirm(userId);
    }

    // 유저 전체 조회
    @Operation(summary = "전체 유저 조회", description = "전체 유저를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserListConfirmationResponse.class))),
    })
    @GetMapping("/entirety")
    public ResponseEntity<UserListConfirmationResponse> userListConfirmation() {
        return userService.listConfirm();
    }

    // 유저 생성
    @Operation(summary = "회원 가입", description = "새로운 유저를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = UserCreateResponse.class))),
            @ApiResponse(responseCode = "400", description = "(message : \"Fail\", code : 400)\n")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserCreateResponse> userGeneration(@RequestBody UserCreateRequest userCreateRequest) {
        return userService.generateUser(userCreateRequest);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 시도 후 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserSignInResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserSignInResponse> signIn(@RequestBody UserSignInRequest userSignInRequest) {
        return userService.signIn(userSignInRequest);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급", description = "만료된 accessToken을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserTokenRefreshResponse.class))),
            @ApiResponse(responseCode = "400", description = "(message : \"Fail\", code : 400)\n")
    })
    @PostMapping("/tokens-refresh")
    public ResponseEntity<UserTokenRefreshResponse> tokenRefreshGeneration(@RequestBody UserTokenRefreshRequest userTokenRefreshRequest) {
        return userService.tokenRefreshGenerate(userTokenRefreshRequest);
    }

    // 닉네임 변경
    @Operation(summary = "닉네임 변경", description = "유저 닉네임을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserChangeNicknameResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "400", description = "(message : \"Fail\", code : 400)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<UserChangeNicknameResponse> userNicknameModification(@RequestBody UserChangeNicknameRequest userChangeNicknameRequest) {
        return userService.modifyNickname(userChangeNicknameRequest);
    }

    // 프로필 조회
    @Operation(summary = "개인 정보 조회", description = "접속한 유저의 개인정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserProfileConfirmationResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<UserProfileConfirmationResponse> profileConfirmation() {
        return userService.profileConfirm();
    }

    // 알림 설정
    @Operation(summary = "알람 설정", description = "유저 알람 활성화 or 비활성화 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserChangeAlamResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n")
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/notification")
    public ResponseEntity<UserChangeAlamResponse> userAlamModification(@RequestBody UserChangeAlamRequest userChangeAlamRequest) {
        return userService.modifyAlam(userChangeAlamRequest);
    }

    // 유저 삭제
    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserDeleteResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<UserDeleteResponse> userRemoval() {
        return userService.removal();
    }

    // 유저 비활성화
    @Operation(summary = "유저 비활성화", description = "유저를 비활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserDeactivateResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/deactivation")
    public ResponseEntity<UserDeactivateResponse> userDeactivation() {
        return userService.deactivate();
    }

    // 유저아이디 중복확인
    @Operation(summary = "유저아이디 중복확인", description = "유저아이디 중복확인을 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserDuplicateCheckResponse.class)))
    })
    @PostMapping("/id-duplicate")
    public ResponseEntity<UserDuplicateCheckResponse> userDuplicateCheck(@RequestBody UserDuplicateCheckRequest userDuplicateCheckRequest) {
        return userService.duplicateCheck(userDuplicateCheckRequest);
    }

    // 인증 이메일 발송
    @Operation(summary = "인증코드 이메일 발송", description = "이메일로 인증 코드를 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserSendEmailCertificationResponse.class)))
    })
    @PostMapping("/certification-send")
    public ResponseEntity<UserSendEmailCertificationResponse> userSendEmailCertification(@RequestBody UserSendEmailCertificationRequest userSendEmailCertificationRequest) {
        return userService.sendEmailCertificate(userSendEmailCertificationRequest);
    }

    // 인증 코드 확인
    @Operation(summary = "인증 코드 확인", description = "인증 코드 일치여부를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserPassEmailCertificationResponse.class)))
    })
    @PostMapping("/certification-pass")
    public ResponseEntity<UserPassEmailCertificationResponse> userPassEmailCertification(@RequestBody UserPassEmailCertificationRequest userPassEmailCertificationRequest) {
        return userService.passEmailCertificate(userPassEmailCertificationRequest);
    }

    // 비밀번호 변경
    @Operation(summary = "패스워드 변경", description = "새로운 패스워드로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserChangePasswordResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PostMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserChangePasswordResponse> userPasswordModification(@RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        return userService.passwordModify(userChangePasswordRequest);
    }

    // 임시 비밀번호 발급
    @Operation(summary = "임시 비밀번호 발급", description = "해당 이메일로 임시 비밀번호를 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserTemporaryPasswordSendResponse.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PostMapping("/temporary-password")
    public ResponseEntity<UserTemporaryPasswordSendResponse> userTemporaryPasswordSendMail(@RequestBody UserTemporaryPasswordSendRequest userTemporaryPasswordSendRequest) {
        return userService.temporaryPasswordSend(userTemporaryPasswordSendRequest);
    }

    // 유저 열매, 이파리 생성 가능 여부
    @Operation(summary = "유저 열매, 이파리 생성 가능 여부 조회", description = "유저 열매, 이파리 생성 가능 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserCreateStatusResponse.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"Fail\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"Fail\", code : 404)\n")
    })
    @PostMapping("/create-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserCreateStatusResponse> userCreateStatusConfirmation() {
        return userService.createStatusConfirm();
    }

}
