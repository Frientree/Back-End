package com.d101.frientree.serviceImpl.app;

import com.d101.frientree.entity.user.User;
import com.d101.frientree.repository.user.UserRepository;
import com.d101.frientree.service.app.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class FcmServiceImpl implements FcmService {
    private final UserRepository userRepository;

    @Async("taskExecutor")
    @Override
    public void sendNotificationToActiveUsers(String title, String body) {
        List<User> users = userRepository.findActiveUsersWithFruitStatusTrueAndNotificationTrue();

        users.forEach(user -> {
            String fcmToken = user.getFcmToken(); // 사용자로부터 FCM 토큰 얻기

            // FCM 토큰 유효성 검사
            if (fcmToken != null && !fcmToken.isEmpty()) {
                Message message = Message.builder()
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .setToken(fcmToken) // 유효한 토큰만 설정
                        .build();

                try {
                    FirebaseMessaging.getInstance().send(message);
                } catch (FirebaseMessagingException e) {
                    log.error("fcm send error");
                }
            } else {
                // 유효하지 않은 토큰에 대한 처리
                log.warn("Invalid FCM Token for user : {}", user.getUserNickname());
            }
        });
    }

}
