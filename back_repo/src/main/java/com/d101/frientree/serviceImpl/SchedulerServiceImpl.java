package com.d101.frientree.serviceImpl;

import com.d101.frientree.service.FcmService;
import com.d101.frientree.service.LeafService;
import com.d101.frientree.service.UserService;
import com.d101.frientree.service.mongo.MongoEmotionService;
import com.d101.frientree.serviceImpl.userfruit.fastapi.HttpPostAIRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SchedulerServiceImpl {
    private final UserService userService;
    private final LeafService leafService;
    private final FcmService fcmService;
    private final MongoEmotionService mongoEmotionService;
    private final HttpPostAIRequest httpPostAIRequest;

    @Scheduled(cron = "20 0 0 * * *")
    public void performAllScheduledTasks(){
        userService.updateAllUserFruitAndLeafStatus();
        leafService.moveAndDeleteOldLeaves();
    }

    @Scheduled(cron = "0 3 0 * * *")
    public void test() throws Exception {
        String csvFileUrl = mongoEmotionService.makeFileCsv();
        httpPostAIRequest.csvFileS3UploadUrlSend(csvFileUrl);
    }

    @Scheduled(cron = "0 0 10 * * *")
    public void notification(){
        fcmService.sendNotificationToActiveUsers("열매 생성!", "아직 열매를 만들지 않았어요!");
    }
}
/*@Scheduled(cron = "초 분 시 일 월 요일")*/