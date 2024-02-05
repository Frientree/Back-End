package com.d101.frientree.serviceImpl;

import com.d101.frientree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerServiceImpl {
    private final UserService userService;

    @Scheduled(cron = "0 0 1 * * *")
    public void updateUserFruitStatusScheduler(){
        userService.updateAllUserFruitAndLeafStatus();
    }

}

/*@Scheduled(cron = "초 분 시 일 월 요일")*/