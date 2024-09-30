package com.example.delivery.domain.fcm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.delivery.domain.fcm.dto.PushsRequestDto;
import com.example.delivery.domain.rider.dao.DeliveryDao;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FCMServiceTest {

  @Mock private FirebaseMessaging firebaseMessaging;

  @Mock private DeliveryDao deliveryDao;

  @InjectMocks private FCMService fcmService;

  @Test
  void sendMessageDelivery_successful() throws FirebaseMessagingException {
    // Given
    PushsRequestDto requestDto = new PushsRequestDto("Test Title", "Test Content", "Test Address");
    Set<String> tokens = new HashSet<>();
    tokens.add("token1");
    tokens.add("token2");

    when(deliveryDao.getRiderTokensByAddress(requestDto.getAddress())).thenReturn(tokens);

    fcmService.sendMessageDelivery(requestDto);

    verify(deliveryDao, times(1)).getRiderTokensByAddress(requestDto.getAddress());
    verify(firebaseMessaging, times(1)).sendEachForMulticast(any(MulticastMessage.class));
  }

  @Test
  void sendMessageDelivery_firebaseException() throws FirebaseMessagingException {
    // Given
    PushsRequestDto requestDto = new PushsRequestDto("Test Title", "Test Content", "Test Address");
    Set<String> tokens = new HashSet<>();
    tokens.add("token1");
    tokens.add("token2");

    // Mock deliveryDao to return tokens
    when(deliveryDao.getRiderTokensByAddress(requestDto.getAddress())).thenReturn(tokens);

    // Mock FirebaseMessagingException
    FirebaseMessagingException firebaseException = mock(FirebaseMessagingException.class);
    doThrow(firebaseException)
        .when(firebaseMessaging)
        .sendEachForMulticast(any(MulticastMessage.class));

    // When
    try {
      fcmService.sendMessageDelivery(requestDto);
    } catch (FirebaseMessagingException e) {
      // Then
      verify(deliveryDao, times(1)).getRiderTokensByAddress(requestDto.getAddress());
      verify(firebaseMessaging, times(1)).sendEachForMulticast(any(MulticastMessage.class));
    }
  }
}
