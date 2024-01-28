package com.d101.frientree.dto.leaf.response;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LeafViewResponse {


    private String message;
    private Long data;

    public static LeafViewResponse createLeafViewResponse(String message, Long views) {
        return LeafViewResponse.builder()
                .message(message)
                .data(views)
                .build();
    }
}
