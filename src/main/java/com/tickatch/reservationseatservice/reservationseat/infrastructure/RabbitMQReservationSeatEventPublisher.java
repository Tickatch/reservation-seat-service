package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.tickatch.reservationseatservice.reservationseat.application.ReservationSeatEventPublisher;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCanceledEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatPreemptEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatErrorCode;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import io.github.tickatch.common.event.DomainEvent;
import io.github.tickatch.common.event.IntegrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQReservationSeatEventPublisher implements ReservationSeatEventPublisher {

  private final RabbitTemplate rabbitTemplate;

  @Value("${spring.application.name:reservation-seat-service}")
  private String serviceName;

  @Value("${messaging.exchange.reservation-seat:tickatch.reservation-seat}")
  private String reservationSeatExchange;

  @Override
  public void publishPreempt(ReservationSeatPreemptEvent preemptEvent) {
    publishEvent(preemptEvent, "예매 좌석 선점");
  }

  @Override
  public void publishCanceled(ReservationSeatCanceledEvent reservationSeat) {
    publishEvent(reservationSeat, "예매 좌석 취소");
  }

  private void publishEvent(DomainEvent event, String eventDescription) {
    log.info("{} 이벤트 발행 시작: {}", eventDescription, event);

    IntegrationEvent integrationEvent = IntegrationEvent.from(event, serviceName);

    try {
      rabbitTemplate.convertAndSend(
          reservationSeatExchange, event.getRoutingKey(), integrationEvent);
      log.info(
          "{} 이벤트 발행 완료: exchange={}, routingKey={}",
          eventDescription,
          reservationSeatExchange,
          event.getRoutingKey());
    } catch (AmqpException e) {
      log.error(
          "{} 이벤트 발행 실패: exchange={}, routingKey={}, event={}",
          eventDescription,
          reservationSeatExchange,
          event.getRoutingKey(),
          event,
          e);
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_EVENT_PUBLISH_FAILED, e);
    }
  }
}
