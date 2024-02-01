package com.d101.frientree.dto.juice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuiceGenerationRequest {

    private String startDate;
    private String endDate;
}
