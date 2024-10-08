package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

  private final RabbitTemplate rabbitTemplate;

  public void send(PushRequestDto message) {
    rabbitTemplate.convertAndSend("exchange.direct.processing", "processing", message);
  }
}
