package com.d101.frientree.serviceImpl.userfruit.fastapi;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class AIResponse {
    @SerializedName("result")
    private List<String> result;
}
