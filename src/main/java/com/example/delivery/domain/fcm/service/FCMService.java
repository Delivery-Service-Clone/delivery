package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.rider.entity.Rider;
import com.example.delivery.domain.rider.exception.RiderNotFoundByAddressException;
import com.example.delivery.domain.rider.repository.RiderRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {

  private final FirebaseMessaging firebaseMessaging;
  private final RiderRepository riderRepository;

//  public void sendPush(PushRequestDto pushRequestDto) throws FirebaseMessagingException {
//
//    firebaseMessaging.send(makeMessage(userInfo.getUserPushToken(), pushRequestDto.getTitle(),
//        pushRequestDto.getContent()));
//  }

  public void sendPushs(PushsRequestDto pushsRequestDto) throws FirebaseMessagingException {

    // 1. 주소를 기반으로 라이더 조회
    List<Rider> riders = riderRepository.findByAddress(pushsRequestDto.getAddress());
    if(riders.isEmpty()){
      throw new RiderNotFoundByAddressException();
    }

    // 2. 라이더의 FCM 토큰 목록 추출
    List<String> targetUserTokens = riders.stream()
        .map(Rider::getFcmToken)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    // 3. 푸시 메시지 전송
    MulticastMessage message = makeMessages(pushsRequestDto.getTitle(),
        pushsRequestDto.getContent(), targetUserTokens);
    firebaseMessaging.sendMulticast(message);
  }

  private Message makeMessage(String targetToken, String title, String body) {
    Notification notification = Notification
        .builder()
        .setTitle(title)
        .setBody(body)
        .build();
    return Message
        .builder()
        .setNotification(notification)
        .setToken(targetToken)
        .build();
  }

  private MulticastMessage makeMessages(String title, String body,
      List<String> targetTokens) {
    Notification notification = Notification.builder()
        .setTitle(title)
        .setBody(body)
        .build();
    return MulticastMessage.builder()
        .setNotification(notification)
        .addAllTokens(targetTokens)
        .build();
  }
}