package com.tickatch.reservationseatservice.reservationseat.presentation.api;

import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatCreator;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatFinder;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.presentation.dto.ReservationSeatResponse;
import io.github.tickatch.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 예매 좌석 API 컨트롤러.
 *
 * <p>예매 좌석 생성, 조회, 수정 및 상태 변경을 위한 REST API를 제공한다.
 *
 * @author 김형섭
 * @since 1.0.0
 */
@RestController
@RequiredArgsConstructor
public class ReservationSeatApi {

  private final ReservationSeatCreator reservationSeatCreator;
  private final ReservationSeatManager reservationSeatManager;
  private final ReservationSeatFinder reservationSeatFinder;

  /**
   * 예매 좌석 생성 API.
   *
   * <p>요청된 정보를 기반으로 다수의 예매 좌석을 생성한다.
   *
   * @param createRequest 예매 좌석 생성 요청 정보
   * @return 생성된 예매 좌석 응답 목록
   */
  @Operation(summary = "예매 좌석 생성", description = "특정 상품에 대한 예매 좌석 정보들을 일괄 생성합니다.")
  @PostMapping("/api/v1/reservation-seats")
  public ApiResponse<List<ReservationSeatResponse>> create(
      @RequestBody ReservationSeatsCreateRequest createRequest) {
    List<ReservationSeat> reservationSeats = reservationSeatCreator.create(createRequest);

    List<ReservationSeatResponse> response =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();

    return ApiResponse.success(response);
  }

  /**
   * 상품의 예매 좌석 목록 조회 API.
   *
   * <p>특정 상품의 모든 예매 좌석을 조회한다.
   *
   * @param productId 상품 ID
   * @return 예매 좌석 응답 목록
   */
  @Operation(summary = "상품의 예매 좌석 목록 조회", description = "특정 상품에 등록된 모든 예매 좌석을 조회합니다.")
  @GetMapping("/api/v1/reservation-seats")
  public ApiResponse<List<ReservationSeatResponse>> findAllByProductId(
      @RequestParam Long productId) {
    List<ReservationSeat> reservationSeats = reservationSeatFinder.findAllBy(productId);

    List<ReservationSeatResponse> response =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();

    return ApiResponse.success(response);
  }

  /**
   * 예매 좌석 정보 수정 API.
   *
   * <p>좌석 등급 및 가격과 같은 예매 좌석 정보를 변경한다.
   *
   * @param updateRequest 예매 좌석 정보 수정 요청 정보
   */
  @Operation(summary = "예매 좌석 정보 수정", description = "가격, 등급 등 예매 좌석 정보 변경을 처리합니다.")
  @PutMapping("/api/v1/reservation-seats")
  public ApiResponse<Void> update(@RequestBody ReservationSeatInfosUpdateRequest updateRequest) {
    reservationSeatManager.updateReservationSeatInfo(updateRequest);

    return ApiResponse.success();
  }

  /**
   * 예매 좌석 선점 API.
   *
   * <p>예매 좌석을 선점 상태로 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  @Operation(summary = "예매 좌석 선점", description = "예매 좌석을 일시적으로 선점합니다(최종 결제 전 임시 상태).")
  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/preempt")
  public ApiResponse<Void> preempt(@PathVariable Long reservationSeatId) {
    reservationSeatManager.preempt(reservationSeatId);

    return ApiResponse.success();
  }

  /**
   * 예매 좌석 예약 확정 API.
   *
   * <p>선점된 예매 좌석을 예약 확정 상태로 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  @Operation(summary = "예매 좌석 예약 확정", description = "예매 선점 상태의 좌석을 최종 예약으로 확정합니다.")
  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/reserve")
  public ApiResponse<Void> reserve(@PathVariable Long reservationSeatId) {
    reservationSeatManager.reserve(reservationSeatId);

    return ApiResponse.success();
  }

  /**
   * 예매 좌석 예약 취소 API.
   *
   * <p>예매 좌석의 예약 상태를 취소하여 다시 사용 가능 상태로 변경한다.
   *
   * @param reservationSeatId 예매 좌석 ID
   */
  @Operation(summary = "예매 좌석 예약 취소", description = "예매 예약 또는 선점 상태의 좌석을 취소합니다.")
  @PostMapping("/api/v1/reservation-seats/{reservationSeatId}/cancel")
  public ApiResponse<Void> cancel(@PathVariable Long reservationSeatId) {
    reservationSeatManager.cancel(reservationSeatId);

    return ApiResponse.success();
  }
}
