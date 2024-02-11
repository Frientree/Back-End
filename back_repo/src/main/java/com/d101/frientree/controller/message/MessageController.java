package com.d101.frientree.controller.message;

import com.d101.frientree.dto.message.response.MessageResponse;
import com.d101.frientree.service.message.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@CrossOrigin("*")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "위로 메세지 ", description = "나무로부터 위로 메세지를 받아옵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위로메세지 조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "위로메세지 조회 실패 (message : \"Message not Found\", code : 400, data : null)", content = @Content),
    })
    @GetMapping
    public ResponseEntity<MessageResponse> messageConfirm() {
        return messageService.confirm();
    }}

