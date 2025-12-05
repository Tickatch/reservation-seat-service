package com.tickatch.reservationseatservice.reservationseat.domain;

import com.tickatch.reservationseatservice.global.domain.AbstractTimeEntity;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatErrorCode;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.Price;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.SeatInfo;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

@Table(name = "p_reservation_seat")
@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationSeat extends AbstractTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Embedded private ProductId productId;

  @Embedded private SeatInfo seatInfo;

  @Enumerated(EnumType.STRING)
  private ReservationSeatStatus status;

  public static ReservationSeat create(ReservationSeatCreateRequest createRequest) {
    ReservationSeat reservationSeat = new ReservationSeat();

    reservationSeat.productId = ProductId.of(createRequest.productId());
    reservationSeat.seatInfo =
        SeatInfo.of(
            createRequest.seatNumber(), createRequest.grade(), Price.of(createRequest.price()));
    reservationSeat.status = ReservationSeatStatus.AVAILABLE;

    return reservationSeat;
  }

  public void updateSeatInfo(SeatInfoUpdateRequest updateRequest) {
    this.seatInfo =
        SeatInfo.of(
            this.seatInfo.getSeatNumber(), updateRequest.grade(), Price.of(updateRequest.price()));
  }

  public void preempt() {
    checkAvailability();

    this.status = ReservationSeatStatus.PREEMPT;
  }

  public void reserve() {
    checkAvailability();

    this.status = ReservationSeatStatus.RESERVED;
  }

  public void cancel() {
    this.status = ReservationSeatStatus.AVAILABLE;
  }

  public boolean isReservable() {
    return status.isAvailable();
  }

  private void checkAvailability() {
    if (this.status.isUnavailable()) {
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_UNAVAILABLE, this.id);
    }
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) {
      return false;
    }
    ReservationSeat seat = (ReservationSeat) o;
    return getId() != null && Objects.equals(getId(), seat.getId());
  }

  @Override
  public final int hashCode() {
    return Objects.hash(this.id);
  }
}
