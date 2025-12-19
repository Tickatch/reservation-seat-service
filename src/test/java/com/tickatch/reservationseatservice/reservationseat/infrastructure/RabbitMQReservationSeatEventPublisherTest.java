package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCanceledEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatPreemptEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import io.github.tickatch.common.event.IntegrationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class RabbitMQReservationSeatEventPublisherTest {

  @Mock private RabbitTemplate rabbitTemplate;

  @InjectMocks private RabbitMQReservationSeatEventPublisher eventPublisher;

  private static final String SERVICE_NAME = "reservation-seat-service";
  private static final String EXCHANGE = "tickatch.reservation-seat";

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(eventPublisher, "serviceName", SERVICE_NAME);
    ReflectionTestUtils.setField(eventPublisher, "reservationSeatExchange", EXCHANGE);
  }

  @Test
  @DisplayName("예매 좌석 선점 이벤트를 성공적으로 발행한다")
  void publishPreempt_success() {
    ReservationSeatPreemptEvent event = new ReservationSeatPreemptEvent(1L, "S석");

    eventPublisher.publishPreempt(event);

    verify(rabbitTemplate)
        .convertAndSend(eq(EXCHANGE), eq(event.getRoutingKey()), any(IntegrationEvent.class));
  }

  @Test
  @DisplayName("예매 좌석 취소 이벤트를 성공적으로 발행한다")
  void publishCanceled_success() {
    ReservationSeatCanceledEvent event = new ReservationSeatCanceledEvent(1L, "S석");

    eventPublisher.publishCanceled(event);

    verify(rabbitTemplate)
        .convertAndSend(eq(EXCHANGE), eq(event.getRoutingKey()), any(IntegrationEvent.class));
  }

  @Test
  @DisplayName("이벤트 발행 실패 시 EventPublishException을 발생시킨다")
  void publishPreempt_fail_throwsException() {
    ReservationSeatPreemptEvent event = new ReservationSeatPreemptEvent(1L, "S석");

    doThrow(new AmqpException("Connection failed"))
        .when(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    assertThatThrownBy(() -> eventPublisher.publishPreempt(event))
        .isInstanceOf(ReservationSeatException.class)
        .hasCauseInstanceOf(AmqpException.class);
  }

  @Test
  @DisplayName("취소 이벤트 발행 실패 시 EventPublishException을 발생시킨다")
  void publishCanceled_fail_throwsException() {
    ReservationSeatCanceledEvent event = new ReservationSeatCanceledEvent(1L, "S석");

    doThrow(new AmqpException("Connection failed"))
        .when(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    assertThatThrownBy(() -> eventPublisher.publishCanceled(event))
        .isInstanceOf(ReservationSeatException.class)
        .hasCauseInstanceOf(AmqpException.class);
  }

  @Test
  @DisplayName("IntegrationEvent가 올바르게 생성되어 발행된다")
  void publishEvent_createsCorrectIntegrationEvent() {
    ReservationSeatPreemptEvent event = new ReservationSeatPreemptEvent(1L, "S석");

    eventPublisher.publishPreempt(event);

    verify(rabbitTemplate)
        .convertAndSend(eq(EXCHANGE), eq(event.getRoutingKey()), any(IntegrationEvent.class));
  }
}
