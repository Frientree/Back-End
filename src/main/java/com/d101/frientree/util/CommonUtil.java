package com.d101.frientree.util;

import com.d101.frientree.entity.user.User;
import com.d101.frientree.exception.server.ServerInspectionTimeException;
import com.d101.frientree.exception.user.UserNotFoundException;
import com.d101.frientree.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CommonUtil {
    private final UserRepository userRepository;

    @Autowired
    public CommonUtil(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getUser(){
        // 현재 접속한 유저 정보를 가져오는 메서드
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();
        User currentUser = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        if (currentUser.getUserDisabled()) {
            throw new UserNotFoundException("user disabled");
        }
        return currentUser;
    }

    public void checkServerInspectionTime(){
        // 현재 시간을 확인
        LocalTime now = LocalTime.now();

        // 점검 시간 설정 (00:00 ~ 00:10)
        LocalTime start = LocalTime.of(0, 0);
        LocalTime end = LocalTime.of(0, 10);

        // 현재 시간이 점검 시간 내에 있는지 확인
        if (now.isAfter(start) && now.isBefore(end)) {
            // 현재 시간이 점검 시간에 해당하면 503 에러를 던짐
            throw new ServerInspectionTimeException("Server Inspection Time");
        }
    }
}
