package com.d101.frientree.controller;

import com.d101.frientree.dto.userdto.*;
import com.d101.frientree.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 유저 개별 조회
    @Operation(summary = "단일 유저 조회", description = "개별 유저를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserConfirmationRequestDTO.class))),
            @ApiResponse(responseCode = "404", description = "(message : \"해당 유저가 존재하지 않습니다.\", code : 404)\n")
    })
    @GetMapping("/{id}")
    private ResponseEntity<UserConfirmationRequestDTO> userConfirmation(@PathVariable Long id){
        UserConfirmationRequestDTO user = userService.getUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    // 유저 전체 조회
    @Operation(summary = "전체 유저 조회", description = "전체 유저를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserConfirmationRequestDTO.class))),
    })
    @GetMapping("/entirety")
    private ResponseEntity<List<UserConfirmationRequestDTO>> userListConfirmation() {
        List<UserConfirmationRequestDTO> allUsers = userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    // 유저 생성
    @Operation(summary = "신규 유저 생성", description = "새로운 유저를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "(message : \"Success\", code : 201)", content = @Content(schema = @Schema(implementation = UserCreateResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "(message : \"입력 정보를 다시 확인해 주세요.\", code : 400)\n")
    })
    @PostMapping("/create")
    public ResponseEntity<UserCreateResponseDTO> userGeneration(@Valid @RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UserCreateResponseDTO createdUser = userService.createUser(userCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // 로그인
    @Operation(summary = "로그인", description = "로그인 시도 후 토큰을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserSignInResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "(message : \"비밀번호가 일치하지 않습니다.\", code : 401)\n"),
            @ApiResponse(responseCode = "404", description = "(message : \"해당 유저는 존재하지 않습니다.\", code : 404)\n")
    })
    @PostMapping("/sign-in")
    public ResponseEntity<UserSignInResponseDTO> signIn(@RequestBody UserSignInRequestDTO userSignInRequestDTO) {
        UserSignInResponseDTO tokenData = userService.signIn(userSignInRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(tokenData);
    }

    // 토큰 재발급
    @Operation(summary = "토큰 재발급", description = "만료된 accessToken을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "(message : \"Success\", code : 200)",
                    content = @Content(schema = @Schema(implementation = UserSignInResponseDTO.class)))
    })
    @PostMapping("/tokens-refresh")
    public ResponseEntity<UserTokenRefreshResponseDTO> tokenRefresh(@RequestBody UserTokenRefreshRequestDTO userTokenRefreshRequestDTO) {
        UserTokenRefreshResponseDTO userTokenRefreshResponseDTO = userService.tokenRefresh(userTokenRefreshRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userTokenRefreshResponseDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping
    public ResponseEntity<UserChangeNicknameResponseDTO> userNicknameModification(@RequestBody UserChangeNicknameRequestDTO userChangeNicknameRequestDTO) {
        UserChangeNicknameResponseDTO newNickname = userService.changeUserNickname(userChangeNicknameRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(newNickname);
    }

}
