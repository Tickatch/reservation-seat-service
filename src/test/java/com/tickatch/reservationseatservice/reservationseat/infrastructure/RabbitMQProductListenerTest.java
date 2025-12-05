package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import static org.mockito.Mockito.verify;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RabbitMQProductListenerTest {

  @Mock private ReservationSeatManager reservationSeatManager;

  @InjectMocks private RabbitMQProductListener rabbitMQProductListener;

  @Test
  void deleteByProductRequest() {
    rabbitMQProductListener.deleteByProductRequest(new ProductReservationSeatDeleteRequest(1L));

    verify(reservationSeatManager).delete(1L);
  }
}
