package com.d101.frientree.dto.userfruit.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFruitTextRequest {
    @Schema(description = "사용자 입력 텍스트", example = "오늘도 화이팅해보자!")
    private String content;
}
