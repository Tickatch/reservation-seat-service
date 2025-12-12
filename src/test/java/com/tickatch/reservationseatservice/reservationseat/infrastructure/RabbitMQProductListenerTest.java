package com.tickatch.reservationseatservice.reservationseat.infrastructure;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import io.github.tickatch.common.event.IntegrationEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RabbitMQProductListenerTest {

  @Mock private ReservationSeatManager reservationSeatManager;

  @InjectMocks private RabbitMQProductListener rabbitMQProductListener;

  @Mock private IntegrationEvent integrationEvent;

  @Test
  void deleteByProductRequest() {
    Long productId = 1L;
    ProductReservationSeatDeleteRequest payload =
        new ProductReservationSeatDeleteRequest(productId);

    when(integrationEvent.getPayloadAs(ProductReservationSeatDeleteRequest.class))
        .thenReturn(payload);

    rabbitMQProductListener.deleteByProductRequest(integrationEvent);

    verify(integrationEvent).getPayloadAs(ProductReservationSeatDeleteRequest.class);
    verify(reservationSeatManager).delete(productId);
  }
}
