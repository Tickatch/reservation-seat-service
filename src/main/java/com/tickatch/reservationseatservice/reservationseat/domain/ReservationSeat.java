package com.tickatch.reservationseatservice.reservationseat.domain;

import com.tickatch.reservationseatservice.global.domain.AbstractTimeEntity;
import java.util.UUID;

public class ReservationSeat extends AbstractTimeEntity {
  private Long id;

  private String seatNumber;

  private UUID productId;

  private String grade;

  private Price price;

  private ReservationSeatStatus status;
}
