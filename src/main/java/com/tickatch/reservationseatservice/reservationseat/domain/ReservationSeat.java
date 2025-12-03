package com.tickatch.reservationseatservice.reservationseat.domain;

import com.tickatch.reservationseatservice.global.domain.AbstractTimeEntity;
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
  @Getter(AccessLevel.NONE)
  private Long id;

  @Embedded private ProductId productId;

  @Embedded private SeatInfo seatInfo;

  @Enumerated(EnumType.STRING)
  private ReservationSeatStatus status;

  public ReservationSeatId getId() {
    return ReservationSeatId.of(this.id);
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
