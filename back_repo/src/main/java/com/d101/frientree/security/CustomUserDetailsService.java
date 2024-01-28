package com.d101.frientree.security;

import com.d101.frientree.dto.user.UserDTO;
import com.d101.frientree.entity.user.User;
import com.d101.frientree.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
@Log4j2
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findById(Long.valueOf(username))
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저는 존재하지 않습니다."));

        if (user.getUserDisabled()) {
            throw new DisabledException("해당 유저는 비활성화 상태입니다.");
        }

        List<String> roles = Collections.singletonList("USER");
        return new UserDTO(String.valueOf(user.getUserId()), user.getUserPassword(), roles);
    }

}
