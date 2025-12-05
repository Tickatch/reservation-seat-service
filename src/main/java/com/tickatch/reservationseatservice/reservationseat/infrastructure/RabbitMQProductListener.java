package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 상품 관련 메시지를 수신하여 예매 좌석을 처리하는 RabbitMQ 리스너.
 *
 * <p>상품 삭제 이벤트를 수신하면 해당 상품에 속한 예매 좌석을 모두 삭제한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMQProductListener {

  private final ReservationSeatManager reservationSeatManager;

  /**
   * 상품 삭제 이벤트에 따른 예매 좌석 삭제 처리.
   *
   * <p>RabbitMQ를 통해 전달된 상품 삭제 메시지를 수신하고, 해당 상품의 모든 예매 좌석을 삭제한다.
   *
   * @param request 예매 좌석 삭제를 위한 상품 정보 요청
   */
  @RabbitListener(queues = "tickatch.product.cancelled.reservation-seat.queue")
  public void deleteByProductRequest(ProductReservationSeatDeleteRequest request) {
    log.info("상품 삭제 메세지 수신 - 상품[{}] 예매 좌석 삭제 시작", request.getProductId());
    reservationSeatManager.delete(request.getProductId());
    log.info("상품[{}] 예매 좌석 삭제 완료", request.getProductId());
  }
}
