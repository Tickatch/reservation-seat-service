package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.tickatch.reservationseatservice.global.config.ActorExtractor;
import com.tickatch.reservationseatservice.global.config.ActorExtractor.ActorInfo;
import com.tickatch.reservationseatservice.reservationseat.application.ReservationSeatLogEventPublisher;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatErrorCode;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQReservationSeatLogEventPublisher implements ReservationSeatLogEventPublisher {

  private static final String ROUTING_KEY = "reservation-seat.log";

  private final RabbitTemplate rabbitTemplate;

  @Value("${messaging.log.exchange:tickatch.log}")
  private String reservationSeatExchange;

  @Override
  public void publish(Long reservationSeatId, String seatNumber, String actionType) {
    ActorInfo actor = ActorExtractor.extract();

    ReservationSeatLogEvent logEvent =
        new ReservationSeatLogEvent(
            UUID.randomUUID(),
            reservationSeatId,
            seatNumber,
            actionType,
            actor.actorType(),
            actor.actorUserId(),
            LocalDateTime.now());

    publishEvent(logEvent);
  }

  private void publishEvent(ReservationSeatLogEvent event) {
    log.info("{} 로그 이벤트 발행 시작", event.actionType());

    try {
      rabbitTemplate.convertAndSend(reservationSeatExchange, ROUTING_KEY, event);
      log.info(
          "{} 이벤트 발행 완료: exchange={}, routingKey={}",
          event.actionType(),
          reservationSeatExchange,
          ROUTING_KEY);
    } catch (AmqpException e) {
      log.error(
          "{} 이벤트 발행 실패: exchange={}, routingKey={}, event={}",
          event.actionType(),
          reservationSeatExchange,
          ROUTING_KEY,
          event,
          e);
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_EVENT_PUBLISH_FAILED, e);
    }
  }
}
