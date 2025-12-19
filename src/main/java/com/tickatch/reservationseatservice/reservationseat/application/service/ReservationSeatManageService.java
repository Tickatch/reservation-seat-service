package com.tickatch.reservationseatservice.reservationseat.application.service;

import com.tickatch.reservationseatservice.reservationseat.application.ReservationSeatActionType;
import com.tickatch.reservationseatservice.reservationseat.application.ReservationSeatEventPublisher;
import com.tickatch.reservationseatservice.reservationseat.application.ReservationSeatLogEventPublisher;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCanceledEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatPreemptEvent;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Transactional
@RequiredArgsConstructor
@Service
@Validated
public class ReservationSeatManageService implements ReservationSeatManager {

  private final ReservationSeatFinder reservationSeatFinder;
  private final ReservationSeatRepository reservationSeatRepository;
  private final ReservationSeatEventPublisher reservationSeatEventPublisher;
  private final ReservationSeatLogEventPublisher reservationSeatLogEventPublisher;

  @Override
  public void updateReservationSeatInfo(ReservationSeatInfosUpdateRequest updateRequest) {
    List<Long> reservationSeatIds = updateRequest.getReservationSeatIds();

    List<ReservationSeat> reservationSeats =
        reservationSeatRepository.findAllByIdIn(reservationSeatIds);
    Map<Long, ReservationSeat> reservationSeatMap =
        reservationSeats.stream()
            .collect(Collectors.toMap(ReservationSeat::getId, Function.identity()));

    updateRequest
        .seatInfos()
        .forEach(
            updateInfo -> {
              ReservationSeat seat = reservationSeatMap.get(updateInfo.reservationSeatId());
              seat.updateSeatInfo(updateInfo.toUpdateRequest());
            });

    reservationSeatRepository.saveAll(reservationSeats);
  }

  @Override
  public void preempt(Long reservationSeatId, UUID requestId) {
    ReservationSeat reservationSeat = reservationSeatFinder.findByIdWithLock(reservationSeatId);

    reservationSeat.preempt(requestId);

    ReservationSeat saved = reservationSeatRepository.save(reservationSeat);

    reservationSeatEventPublisher.publishPreempt(
        new ReservationSeatPreemptEvent(
            reservationSeat.getProductId().id(), reservationSeat.getSeatInfo().getGrade()));

    reservationSeatLogEventPublisher.publish(
        saved.getId(),
        saved.getSeatInfo().getSeatNumber(),
        ReservationSeatActionType.PREEMPT.name());
  }

  @Override
  public void reserve(Long reservationSeatId, UUID requestId) {
    ReservationSeat reservationSeat = reservationSeatFinder.findByIdWithLock(reservationSeatId);

    reservationSeat.reserve(requestId);

    ReservationSeat saved = reservationSeatRepository.save(reservationSeat);

    reservationSeatLogEventPublisher.publish(
        saved.getId(),
        saved.getSeatInfo().getSeatNumber(),
        ReservationSeatActionType.RESERVE.name());
  }

  @Override
  public void cancel(Long reservationSeatId, UUID requestId) {
    ReservationSeat reservationSeat = reservationSeatFinder.findByIdWithLock(reservationSeatId);

    reservationSeat.cancel(requestId);

    ReservationSeat saved = reservationSeatRepository.save(reservationSeat);

    reservationSeatEventPublisher.publishCanceled(
        new ReservationSeatCanceledEvent(
            reservationSeat.getProductId().id(), reservationSeat.getSeatInfo().getGrade()));

    reservationSeatLogEventPublisher.publish(
        saved.getId(),
        saved.getSeatInfo().getSeatNumber(),
        ReservationSeatActionType.CANCEL.name());
  }

  @Override
  public void delete(Long productId) {
    reservationSeatRepository.deleteAllByProductId(ProductId.of(productId));
  }
}
