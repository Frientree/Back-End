package com.d101.frientree.dto.leaf.response.dto;

import com.d101.frientree.entity.LeafCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeafCreateRequestDTO {

    private LeafCategory leafCategory;

    private String leafContent;

}
