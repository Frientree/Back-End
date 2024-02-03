package com.d101.frientree.dto.message.response.dto;

import com.d101.frientree.entity.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private String messageDescription;

}
