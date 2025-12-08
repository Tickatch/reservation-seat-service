package com.tickatch.reservationseatservice.reservationseat.domain;

import static org.springframework.util.Assert.*;

import com.tickatch.reservationseatservice.global.domain.AbstractTimeEntity;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.ReservationSeatCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.dto.SeatInfoUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatErrorCode;
import com.tickatch.reservationseatservice.reservationseat.domain.exception.ReservationSeatException;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.Price;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import com.tickatch.reservationseatservice.reservationseat.domain.vo.ReserverId;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/**
 * 예매 좌석 엔티티.
 *
 * <p>상품에 속한 좌석 정보, 좌석 등급 및 가격, 예매 상태 등을 관리한다. 예매 좌석은 <b>선점</b>, <b>예약</b>, <b>취소</b>와 같은 상태 변경 기능을
 * 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
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

  @Embedded private ReserverId reserverId;

  /**
   * 예매 좌석 생성.
   *
   * <p>생성 요청 정보를 기반으로 예매 좌석을 초기화하며, 좌석 상태는 기본적으로 <code>AVAILABLE</code>(예약 가능) 상태로 등록된다.
   *
   * @param createRequest 예매 좌석 생성 요청 정보
   * @return 생성된 예매 좌석
   */
  public static ReservationSeat create(ReservationSeatCreateRequest createRequest) {
    ReservationSeat reservationSeat = new ReservationSeat();

    reservationSeat.productId = ProductId.of(createRequest.productId());
    reservationSeat.seatInfo =
        SeatInfo.of(
            createRequest.seatNumber(), createRequest.grade(), Price.of(createRequest.price()));
    reservationSeat.status = ReservationSeatStatus.AVAILABLE;

    return reservationSeat;
  }

  /**
   * 예매 좌석 정보 수정.
   *
   * <p>기존 좌석 번호는 유지하며 등급 및 가격 정보를 변경한다.
   *
   * @param updateRequest 변경할 좌석 정보
   */
  public void updateSeatInfo(SeatInfoUpdateRequest updateRequest) {
    this.seatInfo =
        SeatInfo.of(
            this.seatInfo.getSeatNumber(), updateRequest.grade(), Price.of(updateRequest.price()));
  }

  /**
   * 예매 좌석 선점.
   *
   * <p>사용자가 해당 좌석을 일정 시간 동안 점유할 수 있도록 상태를 <code>PREEMPT</code>로 변경한다. 이미 사용 불가 상태일 경우 예외가 발생한다.
   *
   * @param requestId 요청자 ID
   * @throws ReservationSeatException 좌석이 예약 불가 상태일 때
   */
  public void preempt(UUID requestId) {
    canPreempt();

    this.reserverId = ReserverId.of(requestId);
    this.status = ReservationSeatStatus.PREEMPT;
  }

  /**
   * 예매 좌석 예약 확정.
   *
   * <p>선점된 예매 좌석을 실제 예약 상태인 <code>RESERVED</code>로 변경한다. 선점 상태가 아니거나 요청자가 예약자가 아닌 경우 예외가 발생한다.
   *
   * @param requestId 요청자 ID
   * @throws ReservationSeatException 좌석이 예약 불가 상태일 때
   * @throws IllegalArgumentException 요청자가 예약자가 아닌 경우
   */
  public void reserve(UUID requestId) {
    canReserve(requestId);

    this.status = ReservationSeatStatus.RESERVED;
  }

  /**
   * 예매 좌석 예약 취소.
   *
   * <p>예약 또는 선점된 좌석을 <code>AVAILABLE</code> 상태로 되돌린다.
   *
   * @param requestId 요청자 ID
   * @throws IllegalArgumentException 요청자가 예약자가 아닌 경우
   */
  public void cancel(UUID requestId) {
    canCancel(requestId);

    this.reserverId = null;
    this.status = ReservationSeatStatus.AVAILABLE;
  }

  /**
   * 좌석이 선점 가능한 상태인지 확인.
   *
   * <p>해당 좌석이 <b>AVAILABLE</b> 상태인지 여부를 반환한다.
   *
   * @return 예약 가능 여부
   */
  public boolean isPreemptable() {
    return status.isAvailable();
  }

  private void canPreempt() {
    if (this.status.isUnavailable()) {
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_UNAVAILABLE, this.id);
    }
  }

  private void canReserve(UUID requestId) {
    if (!this.status.isPreempt()) {
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_UNAVAILABLE, this.id);
    }
    checkRequestIdIsReserver(requestId);
  }

  private void canCancel(UUID requestId) {
    if (this.status.isAvailable()) {
      throw new ReservationSeatException(
          ReservationSeatErrorCode.RESERVATION_SEAT_ALREADY_AVAILABLE, this.id);
    }
    checkRequestIdIsReserver(requestId);
  }

  private void checkRequestIdIsReserver(UUID requestId) {
    isTrue(isReservedBy(requestId), "요청 권한이 없습니다.");
  }

  private boolean hasReserver() {
    return this.reserverId != null;
  }

  private boolean isReservedBy(UUID requestId) {
    return hasReserver() && this.reserverId.toUuid().equals(requestId);
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
