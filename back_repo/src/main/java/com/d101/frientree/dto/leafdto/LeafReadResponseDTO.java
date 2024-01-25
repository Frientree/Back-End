package com.d101.frientree.dto.leafdto;


import com.d101.frientree.entity.LeafCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeafReadResponseDTO {

    private Long leafNum;

    private String leafContent;

    private LeafCategory leafCategory;

}
