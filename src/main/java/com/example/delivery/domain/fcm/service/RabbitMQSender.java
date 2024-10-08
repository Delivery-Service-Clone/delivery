package com.example.delivery.domain.fcm.service;

import com.example.delivery.domain.fcm.dto.PushRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQSender {

  private final RabbitTemplate rabbitTemplate;

  public void send(PushRequestDto message) {
    rabbitTemplate.convertAndSend("exchange.direct.processing", "processing", message);
  }
}