package com.d101.frientree.controller.user;

import com.d101.frientree.dto.user.request.*;
import com.d101.frientree.dto.user.response.*;
import com.d101.frientree.service.user.UserService;
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
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
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
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = UserGenerationResponse.class))),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Email, Nickname, Password validation error", code : 400)""", content = @Content)
    })
    @PostMapping("/sign-up")
    public ResponseEntity<UserGenerationResponse> userGeneration(@RequestBody UserGenerationRequest userGenerationRequest) {
        return userService.userGenerate(userGenerationRequest);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 시도 후 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserSignInResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Password Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserSignInResponse> userSignIn(@RequestBody UserSignInRequest userSignInRequest) {
        return userService.signIn(userSignInRequest);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급", description = "만료된 accessToken을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserTokenRefreshGenerationResponse.class))),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Refresh Token Error", code : 400)""", content = @Content)
    })
    @PostMapping("/tokens-refresh")
    public ResponseEntity<UserTokenRefreshGenerationResponse> userTokenRefreshGeneration(@RequestBody UserTokenRefreshGenerationRequest userTokenRefreshGenerationRequest) {
        return userService.tokenRefreshGenerate(userTokenRefreshGenerationRequest);
    }

    // 닉네임 변경
    @Operation(summary = "닉네임 변경", description = "유저 닉네임을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserNicknameModificationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Nickname validation Error", code : 400)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<UserNicknameModificationResponse> userNicknameModification(@RequestBody UserNicknameModificationRequest userNicknameModificationRequest) {
        return userService.nicknameModify(userNicknameModificationRequest);
    }

    // 프로필 조회
    @Operation(summary = "개인 정보 조회", description = "접속한 유저의 개인정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserProfileConfirmationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<UserProfileConfirmationResponse> userProfileConfirmation() {
        return userService.profileConfirm();
    }

    // 알림 설정
    @Operation(summary = "알람 설정", description = "유저 알람 활성화 or 비활성화 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserAlamModificationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/notification")
    public ResponseEntity<UserAlamModificationResponse> userAlamModification(@RequestBody UserAlamModificationRequest userAlamModificationRequest) {
        return userService.alamModify(userAlamModificationRequest);
    }

    // 유저 삭제
    @Operation(summary = "유저 삭제", description = "유저를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserRemovalResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping
    public ResponseEntity<UserRemovalResponse> userRemoval() {
        return userService.remove();
    }

    // 유저 비활성화
    @Operation(summary = "유저 비활성화", description = "유저를 비활성화합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserDeactivationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/deactivation")
    public ResponseEntity<UserDeactivationResponse> userDeactivation() {
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
                    content = @Content(schema = @Schema(implementation = UserPassEmailCertificationResponse.class))),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Code Error", code : 400)""", content = @Content)
    })
    @PostMapping("/certification-pass")
    public ResponseEntity<UserPassEmailCertificationResponse> userPassEmailCertification(@RequestBody UserPassEmailCertificationRequest userPassEmailCertificationRequest) {
        return userService.passEmailCertificate(userPassEmailCertificationRequest);
    }

    // 비밀번호 변경
    @Operation(summary = "패스워드 변경", description = "새로운 패스워드로 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserPasswordModificationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content),
            @ApiResponse(responseCode = "400", description = """
                    (message : "Current Password Not match", code : 400)""", content = @Content),
            @ApiResponse(responseCode = "422", description = """
                    (message : "New Password Validation Error", code : 422)""", content = @Content)
    })
    @PostMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPasswordModificationResponse> userPasswordModification(@RequestBody UserPasswordModificationRequest userPasswordModificationRequest) {
        return userService.passwordModify(userPasswordModificationRequest);
    }

    // 임시 비밀번호 발급
    @Operation(summary = "임시 비밀번호 발급", description = "해당 이메일로 임시 비밀번호를 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserTemporaryPasswordSendMailResponse.class))),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @PostMapping("/temporary-password")
    public ResponseEntity<UserTemporaryPasswordSendMailResponse> userTemporaryPasswordSendMail(@RequestBody UserTemporaryPasswordSendMailRequest userTemporaryPasswordSendMailRequest) {
        return userService.temporaryPasswordSend(userTemporaryPasswordSendMailRequest);
    }

    // 유저 열매, 이파리 생성 가능 여부
    @Operation(summary = "유저 열매, 이파리 생성 가능 여부 조회", description = "유저 열매, 이파리 생성 가능 여부를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserCreateStatusConfirmationResponse.class))),
            @ApiResponse(responseCode = "401", description = """
                    (message : "Access Token Error", code : 401)""", content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    (message : "User not found", code : 404)""", content = @Content)
    })
    @GetMapping("/create-status")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserCreateStatusConfirmationResponse> userCreateStatusConfirmation() {
        return userService.createStatusConfirm();
    }

    // 네이버 소셜 로그인
    @Operation(summary = "네이버 소셜 로그인", description = "네이버 계정으로 소셜 로그인을 진행합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserCreateStatusConfirmationResponse.class)))
    })
    @PostMapping("/sign-in-naver")
    public ResponseEntity<UserSignInNaverResponse> userSignInNaver(@RequestBody UserSignInNaverRequest userSignInNaverRequest) {
        return userService.userSignInNaver(userSignInNaverRequest);
    }

    // Fcm 알림 토큰
    @Operation(summary = "FCM 토큰", description = "FCM 토큰을 업데이트 합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserCreateStatusConfirmationResponse.class)))
    })
    @PostMapping("/update-fcm-token")
    public ResponseEntity<UserFcmTokenUpdateResponse> updateFcmToken(@RequestBody UserFcmTokenUpdateRequest request){
        return userService.updateFcmToken(request);
    }
}
