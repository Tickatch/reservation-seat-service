package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import jakarta.validation.Valid;
import java.util.List;

public interface ReservationSeatCreator {
  List<ReservationSeat> create(@Valid ReservationSeatsCreateRequest createRequest);
}
