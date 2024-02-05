package com.d101.frientree.serviceImpl;

import com.d101.frientree.service.LeafService;
import com.d101.frientree.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchedulerServiceImpl {
    private final UserService userService;
    private final LeafService leafService;

    @Scheduled(cron = "20 0 0 * * *")
    public void performAllScheduledTasks(){
        userService.updateAllUserFruitAndLeafStatus();
        leafService.moveAndDeleteOldLeaves();
    }
}
/*@Scheduled(cron = "초 분 시 일 월 요일")*/