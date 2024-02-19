package com.d101.frientree.dto.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.stream.Collectors;

// Spring security의 UserDetails를 구현한 User 클래스를 상속한 jwt에 사용되는 DTO 클래스입니다.
public class UserDTO extends User {


    public UserDTO(String username, String password, List<String> roleNames) {
        super(
                String.valueOf(username),
                password,
                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));
    }


}