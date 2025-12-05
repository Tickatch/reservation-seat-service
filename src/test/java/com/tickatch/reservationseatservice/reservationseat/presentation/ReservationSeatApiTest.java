package com.tickatch.reservationseatservice.reservationseat.presentation;

import static com.tickatch.reservationseatservice.AssertThatUtils.isTrue;
import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatInfosUpdateRequest;
import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatsCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatCreator;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatManager;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatInfosUpdateRequest;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.presentation.api.dto.ReservationSeatResponse;
import io.github.tickatch.common.security.test.MockUser;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

@SpringBootTest
@AutoConfigureMockMvc
class ReservationSeatApiTest {

  @Autowired MockMvcTester mvcTester;

  @Autowired ObjectMapper objectMapper;

  @MockitoBean ReservationSeatCreator reservationSeatCreator;

  @MockitoBean ReservationSeatManager reservationSeatManager;

  @Test
  @MockUser
  void create() throws JsonProcessingException {
    ReservationSeatsCreateRequest request = createReservationSeatsCreateRequest();

    List<ReservationSeat> reservationSeats =
        request.toCreateRequests().stream()
            .map(ReservationSeatFixture::createMockReservationSeat)
            .toList();

    given(reservationSeatCreator.create(any(ReservationSeatsCreateRequest.class)))
        .willReturn(reservationSeats);
    String requestJson = objectMapper.writeValueAsString(request);

    List<ReservationSeatResponse> expectedResponses =
        reservationSeats.stream().map(ReservationSeatResponse::from).toList();
    String expectedJson = objectMapper.writeValueAsString(expectedResponses);

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/api/v1/reservation-seats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result)
        .hasStatusOk()
        .bodyJson()
        .hasPathSatisfying("$.success", isTrue())
        .extractingPath("$.data")
        .isEqualTo(objectMapper.readValue(expectedJson, List.class));
  }

  @Test
  @MockUser
  void updateReservationSeatInfo() throws JsonProcessingException {
    doNothing()
        .when(reservationSeatManager)
        .updateReservationSeatInfo(any(ReservationSeatInfosUpdateRequest.class));

    ReservationSeatInfosUpdateRequest request = createReservationSeatInfosUpdateRequest(1L, 2L, 3L);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcTestResult result =
        mvcTester
            .put()
            .uri("/api/v1/reservation-seats")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson)
            .exchange();

    assertThat(result).hasStatusOk().bodyJson().hasPathSatisfying("$.success", isTrue());
  }

  @Test
  @MockUser
  void preempt() {
    doNothing().when(reservationSeatManager).preempt(anyLong());

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/api/v1/reservation-seats/{reservationSeatId}/preempt", 1L)
            .exchange();

    assertThat(result).hasStatusOk().bodyJson().hasPathSatisfying("$.success", isTrue());
  }

  @Test
  @MockUser
  void reserve() {
    doNothing().when(reservationSeatManager).reserve(anyLong());

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/api/v1/reservation-seats/{reservationSeatId}/preempt", 1L)
            .exchange();

    assertThat(result).hasStatusOk().bodyJson().hasPathSatisfying("$.success", isTrue());
  }

  @Test
  @MockUser
  void cancel() {
    doNothing().when(reservationSeatManager).cancel(anyLong());

    MvcTestResult result =
        mvcTester
            .post()
            .uri("/api/v1/reservation-seats/{reservationSeatId}/preempt", 1L)
            .exchange();

    assertThat(result).hasStatusOk().bodyJson().hasPathSatisfying("$.success", isTrue());
  }
}
