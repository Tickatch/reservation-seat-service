package com.tickatch.reservationseatservice.reservationseat.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.tickatch.reservationseatservice.PersistenceTest;
import com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatRepository;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeatStatus;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@PersistenceTest
class ReservationSeatManagerTest {

  @Autowired ReservationSeatManager reservationSeatManager;
  @Autowired ReservationSeatRepository reservationSeatRepository;
  @Autowired ReservationSeatCreator reservationSeatCreator;
  @Autowired ReservationSeatFinder reservationSeatFinder;
  @Autowired EntityManager em;

  private List<ReservationSeat> reservationSeats;

  @BeforeEach
  void setUp() {
    reservationSeats = createReservationSeats();
  }

  @Test
  void updateReservationSeatInfo() {
    ReservationSeat firstSeat = reservationSeats.get(0);
    ReservationSeat secondSeat = reservationSeats.get(1);
    ReservationSeat thirdSeat = reservationSeats.get(2);

    ReservationSeatInfoUpdateRequest updateInfo1 =
        new ReservationSeatInfoUpdateRequest(firstSeat.getId(), "VIP석", 20000L);
    ReservationSeatInfoUpdateRequest updateInfo2 =
        new ReservationSeatInfoUpdateRequest(secondSeat.getId(), "R석", 15000L);
    ReservationSeatInfoUpdateRequest updateInfo3 =
        new ReservationSeatInfoUpdateRequest(thirdSeat.getId(), "B석", 8000L);

    ReservationSeatInfosUpdateRequest updateRequest =
        new ReservationSeatInfosUpdateRequest(List.of(updateInfo1, updateInfo2, updateInfo3));

    reservationSeatManager.updateReservationSeatInfo(updateRequest);
    em.flush();
    em.clear();

    List<ReservationSeat> updatedSeats =
        reservationSeatRepository.findAllByIdIn(
            List.of(firstSeat.getId(), secondSeat.getId(), thirdSeat.getId()));

    ReservationSeat updatedFirstSeat = findSeatById(updatedSeats, firstSeat.getId());
    assertThat(updatedFirstSeat.getSeatInfo().getGrade()).isEqualTo("VIP석");
    assertThat(updatedFirstSeat.getSeatInfo().getPrice().value()).isEqualTo(20000L);
    assertThat(updatedFirstSeat.getSeatInfo().getSeatNumber())
        .isEqualTo(firstSeat.getSeatInfo().getSeatNumber());

    ReservationSeat updatedSecondSeat = findSeatById(updatedSeats, secondSeat.getId());
    assertThat(updatedSecondSeat.getSeatInfo().getGrade()).isEqualTo("R석");
    assertThat(updatedSecondSeat.getSeatInfo().getPrice().value()).isEqualTo(15000L);
    assertThat(updatedSecondSeat.getSeatInfo().getSeatNumber())
        .isEqualTo(secondSeat.getSeatInfo().getSeatNumber());

    ReservationSeat updatedThirdSeat = findSeatById(updatedSeats, thirdSeat.getId());
    assertThat(updatedThirdSeat.getSeatInfo().getGrade()).isEqualTo("B석");
    assertThat(updatedThirdSeat.getSeatInfo().getPrice().value()).isEqualTo(8000L);
    assertThat(updatedThirdSeat.getSeatInfo().getSeatNumber())
        .isEqualTo(thirdSeat.getSeatInfo().getSeatNumber());
  }

  @Test
  void preempt() {
    ReservationSeat reservationSeat = reservationSeats.getFirst();

    reservationSeatManager.preempt(reservationSeat.getId());
    em.flush();
    em.clear();

    ReservationSeat result = reservationSeatFinder.findById(reservationSeat.getId());
    assertThat(result.getStatus()).isEqualTo(ReservationSeatStatus.PREEMPT);
  }

  @Test
  void reserve() {
    ReservationSeat reservationSeat = reservationSeats.getFirst();

    reservationSeatManager.reserve(reservationSeat.getId());
    em.flush();
    em.clear();

    ReservationSeat result = reservationSeatFinder.findById(reservationSeat.getId());
    assertThat(result.getStatus()).isEqualTo(ReservationSeatStatus.RESERVED);
  }

  @Test
  void cancel() {
    ReservationSeat reservationSeat = reservationSeats.getFirst();
    reservationSeatManager.preempt(reservationSeat.getId());

    reservationSeatManager.cancel(reservationSeat.getId());
    em.flush();
    em.clear();

    ReservationSeat result = reservationSeatFinder.findById(reservationSeat.getId());
    assertThat(result.getStatus()).isEqualTo(ReservationSeatStatus.AVAILABLE);
  }

  private List<ReservationSeat> createReservationSeats() {
    var createRequest = ReservationSeatFixture.createReservationSeatsCreateRequest();
    List<ReservationSeat> seats = reservationSeatCreator.create(createRequest);

    em.flush();
    em.clear();

    return seats;
  }

  private ReservationSeat findSeatById(List<ReservationSeat> seats, Long id) {
    return seats.stream()
        .filter(seat -> seat.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Seat not found: " + id));
  }
}
