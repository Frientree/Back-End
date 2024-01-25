package com.d101.frientree.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConfirmationRequestDTO {

    private Long userId;

    private String userNickname;

    private Date userCreateDate;

    private String userEmail;

}
