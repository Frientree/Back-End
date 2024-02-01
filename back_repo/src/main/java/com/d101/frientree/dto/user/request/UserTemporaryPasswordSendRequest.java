package com.d101.frientree.dto.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTemporaryPasswordSendRequest {

    private String userEmail;
}
