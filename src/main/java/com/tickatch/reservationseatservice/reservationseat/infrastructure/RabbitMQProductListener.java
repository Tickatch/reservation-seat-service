package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProductListener {

  private final ReservationSeatManager reservationSeatManager;

  @RabbitListener(queues = "tickatch.product.cancelled.reservation-seat.queue")
  public void deleteByProductRequest(ProductReservationSeatDeleteRequest request) {
    log.info("상품 삭제 메세지 수신 - 상품[{}] 예매 좌석 삭제 시작", request.getProductId());
    reservationSeatManager.delete(request.getProductId());
    log.info("상품[{}] 예매 좌석 삭제 완료", request.getProductId());
  }
}
