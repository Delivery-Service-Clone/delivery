package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {

  private final FirebaseMessaging firebaseMessaging;
  private final DeliveryDao deliveryDao;

  public void sendPushs(PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {

    Set<String> tokens = deliveryDao.getRiderTokensByAddress(pushsRequestDto.getAddress());

    MulticastMessage message =
        makeMessages(pushsRequestDto.getTitle(), pushsRequestDto.getContent(), tokens);
    firebaseMessaging.sendMulticast(message);
  }

  private MulticastMessage makeMessages(String title, String body, Set<String> targetTokens) {
    Notification notification = Notification.builder().setTitle(title).setBody(body).build();
    return MulticastMessage.builder()
        .setNotification(notification)
        .addAllTokens(targetTokens)
        .build();
  }
}
