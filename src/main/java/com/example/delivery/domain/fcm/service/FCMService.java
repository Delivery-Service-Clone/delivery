package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FCMService {

  private final FirebaseMessaging firebaseMessaging;
  private final DeliveryDao deliveryDao;

  // 라이더들에게 보낼 메시지 알림 메서드
  // redis에서 토큰값을 가져오기 때문에 dto에 토큰 제외
  public void sendMessageDelivery(PushsRequestDto pushsRequestDto)
      throws FirebaseMessagingException {

    Set<String> tokens = deliveryDao.getRiderTokensByAddress(pushsRequestDto.getAddress());
    MulticastMessage message =
        makeMessages(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), tokens);
    firebaseMessaging.sendEachForMulticast(message);
  }

  private void sendFirebaseMessage(PushRequestDto pushRequestDto, String logMessage) {
    try {
      Message message =
          makeMessage(
              pushRequestDto.getTitle(), pushRequestDto.getContent(), pushRequestDto.getToken());
      firebaseMessaging.send(message);
      log.info(logMessage);
    } catch (FirebaseMessagingException e) {
      log.info("Failed to send message due to invalid token: " + pushRequestDto.getToken(), e);
    }
  }

  @RabbitListener(queues = "processingQueue")
  public void sendMessage(PushRequestDto pushRequestDto) {
    sendFirebaseMessage(pushRequestDto, "정상 실행");
  }

  @RabbitListener(queues = "deadLetterQueue")
  public void receiveErrorMessage(PushRequestDto pushRequestDto) {
    sendFirebaseMessage(pushRequestDto, "오류가 발생했을 때");
  }

  private MulticastMessage makeMessages(String title, String body, Set<String> targetTokens) {
    Notification notification = Notification.builder().setTitle(title).setBody(body).build();
    return MulticastMessage.builder()
        .setNotification(notification)
        .addAllTokens(targetTokens)
        .build();
  }

  private Message makeMessage(String title, String body, String token) {
    Notification notification = Notification.builder().setTitle(title).setBody(body).build();
    return Message.builder().setNotification(notification).setToken(token).build();
  }
}
