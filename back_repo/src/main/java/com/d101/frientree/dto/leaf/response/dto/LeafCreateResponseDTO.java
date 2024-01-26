package com.d101.frientree.dto.leaf.response.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeafCreateResponseDTO {

    private boolean isCreated;
}
