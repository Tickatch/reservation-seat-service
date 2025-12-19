package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tickatch.reservationseatservice.global.config.ActorExtractor;
import com.tickatch.reservationseatservice.global.config.ActorExtractor.ActorInfo;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RabbitMQReservationSeatLogEventPublisherTest {

  @Mock private RabbitTemplate rabbitTemplate;

  private static MockedStatic<ActorExtractor> actorExtractor;

  @InjectMocks private RabbitMQReservationSeatLogEventPublisher publisher;

  private static final String EXCHANGE = "tickatch.reservation-seat";
  private static final String ROUTING_KEY = "reservation-seat.log";

  @BeforeAll
  static void setUpAll() {
    actorExtractor = Mockito.mockStatic(ActorExtractor.class);
  }

  @AfterAll
  static void tearDownAll() {
    actorExtractor.close();
  }

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(publisher, "reservationSeatExchange", EXCHANGE);
  }

  @Test
  void publish() {
    ActorInfo actorInfo = new ActorInfo("CONSUMER", UUID.randomUUID());
    when(ActorExtractor.extract()).thenReturn(actorInfo);

    publisher.publish(1L, "A1", "PREEMPT");

    verify(rabbitTemplate)
        .convertAndSend(eq(EXCHANGE), eq(ROUTING_KEY), any(ReservationSeatLogEvent.class));
  }

  @Test
  void publishIfFailed() {
    ActorInfo actorInfo = new ActorInfo("CONSUMER", UUID.randomUUID());
    when(ActorExtractor.extract()).thenReturn(actorInfo);

    doThrow(new AmqpException("Connection failed"))
        .when(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    assertThatThrownBy(() -> publisher.publish(1L, "A1", "PREEMPT"))
        .isInstanceOf(ReservationSeatException.class)
        .hasCauseInstanceOf(AmqpException.class);
  }
}
