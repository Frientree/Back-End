package com.d101.frientree.dto.leaf.request;

import com.d101.frientree.entity.leaf.LeafCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeafGenerationRequest {

    private LeafCategory leafCategory;

    private String leafContent;

}
