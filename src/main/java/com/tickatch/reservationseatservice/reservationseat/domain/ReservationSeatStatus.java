package com.tickatch.reservationseatservice.reservationseat.domain;

public enum ReservationSeatStatus {
  AVAILABLE,
  PREEMPT,
  RESERVED;

  public boolean isUnavailable() {
    return !isAvailable();
  }

  public boolean isAvailable() {
    return this == AVAILABLE;
  }
}
