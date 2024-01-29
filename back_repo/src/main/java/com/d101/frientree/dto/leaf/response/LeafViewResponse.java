package com.d101.frientree.dto.leaf.response;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class LeafViewResponse {

    private Long userId;
    private String message;
    private Long totalLeafViews;

    public static LeafViewResponse createLeafViewResponse(String message, Long totalLeafViews) {
        return LeafViewResponse.builder()
                .message(message)
                .totalLeafViews(totalLeafViews)
                .build();
    }
}
