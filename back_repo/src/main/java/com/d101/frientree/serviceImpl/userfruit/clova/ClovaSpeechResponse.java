package com.d101.frientree.serviceImpl.userfruit.clova;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClovaSpeechResponse {
    private String result;
    private String message;
    private String token;
    // 추가로 필요한 필드들 정의

    @SerializedName("text")
    private String fullText;
}