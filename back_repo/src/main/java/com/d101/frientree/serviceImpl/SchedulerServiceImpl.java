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

    @Scheduled(cron = "20 0 0 * * *") //매일 00:00T20 실행
    public void performAllScheduledTasks(){
        userService.updateAllUserFruitAndLeafStatus();
        leafService.moveAndDeleteOldLeaves();
    }

    @Scheduled(cron = "0 5 0 * * 2") //매주 월요일 00:05 실행
    public void emotionFeedbackTasks() throws Exception {
        //AWS S3 csv 파일 업로드 (MongoDB Emotion 데이터)
        String csvFileUrl = mongoEmotionService.makeFileCsv();
        //Fast API csv 파일 URL 전달 및 S3 csv 파일 삭제까지
        httpPostAIRequest.csvFileS3UploadUrlSend(csvFileUrl);
        //MongoDB Emotion 데이터 전부 삭제
        mongoEmotionService.deleteEmotion();
    }

    @Scheduled(cron = "0 0 22 * * *") //매일 22:00 실행
    public void notification(){
        fcmService.sendNotificationToActiveUsers("열매 생성!", "아직 열매를 만들지 않았어요!");
    }
}
/*@Scheduled(cron = "초 분 시 일 월 요일")*/