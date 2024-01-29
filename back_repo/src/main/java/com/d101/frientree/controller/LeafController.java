package com.d101.frientree.controller;

import com.d101.frientree.dto.leaf.request.LeafGenerationRequest;
import com.d101.frientree.dto.leaf.response.LeafComplaintResponse;
import com.d101.frientree.dto.leaf.response.LeafConfirmationResponse;
import com.d101.frientree.dto.leaf.response.LeafGenerationResponse;
import com.d101.frientree.dto.leaf.response.LeafViewResponse;

import com.d101.frientree.service.LeafService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaf")
@CrossOrigin("*")
public class LeafController {

    private final LeafService leafService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/test")
    public String test() {
        return "test";
    }

    //이파리 랜덤 조회
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이파리 랜덤 조회", description = "선택한 카테고리에 해당하는 이파리를 랜덤으로 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = LeafConfirmationResponse.class))),
    })
    @GetMapping("/{category}")
    public ResponseEntity<LeafConfirmationResponse> leafConfirmation(@PathVariable String category){
        return leafService.confirm(category);
    }


    // 이파리 작성
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이파리 생성", description = "이파리 카테고리와 내용을 기입해 이파리를 생성합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이파리 생성 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = LeafGenerationResponse.class))),
            @ApiResponse(responseCode = "404", description = "이파리 생성 실패 (message : \"Bad Request\", code : 400, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "이파리 생성 실패 (message : \"Server Error\", code : 500, data : null)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<LeafGenerationResponse> leafGeneration(@RequestBody LeafGenerationRequest leafGenerationRequest) {
        return leafService.generate(leafGenerationRequest);
    }


    // 이파리 신고

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "이파리 신고", description = "이파리의 신고 횟수를 증가 시킵니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이파리 신고 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = LeafViewResponse.class))),
            @ApiResponse(responseCode = "404", description = "이파리 신고 실패 (message : \"Leaf not Found\", code : 400, data : null)", content = @Content),
            @ApiResponse(responseCode = "500", description = "이파리 신고 실패 (message : \"Server Error\", code : 500, data : null)", content = @Content)
    })    @PostMapping("/{leafId}")
    public ResponseEntity<LeafComplaintResponse> leafComplaint(@PathVariable Long leafId){
        return leafService.complain(leafId);
    }

    @Operation(summary = "이파리 조회수 확인", description = "이파리의 전체 조회수를 확인합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이파리 조회 성공 (message : \"Success\", code : 200)", content = @Content(schema = @Schema(implementation = LeafViewResponse.class))),
            @ApiResponse(responseCode = "404", description = "이파리 조회 실패 (message : \"Leaf not Found\", code : 400, data : null)", content = @Content),
    })
    @GetMapping("/view")
    public ResponseEntity<LeafViewResponse> leafView(Long userId){
        return leafService.view(userId);
    }

}
