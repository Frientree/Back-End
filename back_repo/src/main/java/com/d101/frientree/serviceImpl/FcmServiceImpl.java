package com.d101.frientree.serviceImpl;

import com.d101.frientree.entity.user.User;
import com.d101.frientree.repository.UserRepository;
import com.d101.frientree.service.FcmService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.List;

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

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(fcmToken)
                    .build();

            try {
                FirebaseMessaging.getInstance().send(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
        });
    }
}
