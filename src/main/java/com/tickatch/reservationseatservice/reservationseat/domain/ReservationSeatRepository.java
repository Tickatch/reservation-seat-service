package com.tickatch.reservationseatservice.reservationseat.domain;

import com.tickatch.reservationseatservice.reservationseat.domain.vo.ProductId;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

/**
 * 예매 좌석 저장소 인터페이스.
 *
 * <p>예매 좌석의 조회, 저장, 삭제 및 비관적 락(Pessimistic Lock)을 이용한 조회 기능을 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
public interface ReservationSeatRepository extends Repository<ReservationSeat, Long> {

  /**
   * 예매 좌석 단건 저장.
   *
   * @param reservationSeat 저장할 예매 좌석
   * @return 저장된 예매 좌석
   */
  ReservationSeat save(ReservationSeat reservationSeat);

  /**
   * 예매 좌석 다건 저장.
   *
   * @param reservationSeats 저장할 예매 좌석 목록
   * @param <S> 예매 좌석 타입
   * @return 저장된 예매 좌석 목록
   */
  <S extends ReservationSeat> List<S> saveAll(Iterable<S> reservationSeats);

  /**
   * 예매 좌석 ID로 조회.
   *
   * @param id 예매 좌석 ID
   * @return 조회된 예매 좌석(Optional)
   */
  Optional<ReservationSeat> findById(Long id);

  /**
   * 여러 예매 좌석 ID로 조회.
   *
   * @param ids 예매 좌석 ID 목록
   * @return 조회된 예매 좌석 목록
   */
  List<ReservationSeat> findAllByIdIn(List<Long> ids);

  /**
   * 예매 좌석 ID로 비관적 락(PESSIMISTIC_WRITE)을 적용하여 조회.
   *
   * <p>동시 예약 환경에서 데이터 정합성을 보장하기 위해 사용된다.
   *
   * @param id 예매 좌석 ID
   * @return 조회된 예매 좌석(Optional)
   */
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT rs FROM ReservationSeat rs WHERE rs.id = :id")
  Optional<ReservationSeat> findByIdWithLock(Long id);

  /**
   * 상품 ID로 예매 좌석 목록 조회.
   *
   * @param productId 상품 ID 값 객체
   * @return 상품에 속한 예매 좌석 목록
   */
  List<ReservationSeat> findAllByProductId(ProductId productId);

  /**
   * 상품 ID 기준 예매 좌석 전체 삭제.
   *
   * <p>해당 상품에 포함된 모든 예매 좌석을 제거한다.
   *
   * @param productId 상품 ID 값 객체
   */
  void deleteAllByProductId(ProductId productId);
}
