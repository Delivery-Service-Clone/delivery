package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

  private final RabbitTemplate rabbitTemplate;

  @Value("${rabbitmq.exchange.name}")
  private String exchangeName;

  @Value("${rabbitmq.routing.key}")
  private String routingKey;

  public void send(PushRequestDto message) {
    rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
    System.out.println("Sent message: " + message.getContent());
  }
}
