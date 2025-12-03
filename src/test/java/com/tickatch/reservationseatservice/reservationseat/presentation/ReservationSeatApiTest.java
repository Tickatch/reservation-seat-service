package com.tickatch.reservationseatservice.reservationseat.presentation;

import static com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture.createReservationSeatsCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tickatch.reservationseatservice.reservationseat.ReservationSeatFixture;
import com.tickatch.reservationseatservice.reservationseat.application.service.ReservationSeatCreator;
import com.tickatch.reservationseatservice.reservationseat.application.service.dto.ReservationSeatsCreateRequest;
import com.tickatch.reservationseatservice.reservationseat.domain.ReservationSeat;
import com.tickatch.reservationseatservice.reservationseat.presentation.api.dto.ReservationSeatResponse;
import io.github.tickatch.common.security.test.MockUser;
import java.util.List;
import org.assertj.core.api.Assertions;
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

  @MockitoBean ReservationSeatCreator reservationSeatCreator;

  @Autowired ObjectMapper objectMapper;

  @Test
  @MockUser
  void create() throws JsonProcessingException {
    ReservationSeatsCreateRequest request = createReservationSeatsCreateRequest();

    List<ReservationSeat> reservationSeats =
        request.toCreateRequests().stream()
            .map(ReservationSeatFixture::createReservationSeat)
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
        .hasPathSatisfying("$.success", s -> Assertions.assertThat(s).asBoolean().isTrue())
        .extractingPath("$.data")
        .isEqualTo(objectMapper.readValue(expectedJson, List.class));
  }
}
