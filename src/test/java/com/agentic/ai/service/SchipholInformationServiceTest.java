package com.agentic.ai.service;

import com.agentic.ai.exception.GlobalExceptionHandler;
import com.agentic.ai.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Import(GlobalExceptionHandler.class)
class SchipholInformationServiceTest {

    private SchipholClient client;
    private SchipholInformationService service;

    @Resource
    MockMvc mvc;
    @Resource
    ObjectMapper objectMapper;

    private static final Flight flight =
            new Flight(
                    /* id                   */ "143562061436063980",
                    /* flightName           */ "HV6734",
                    /* flightNumber         */ 6734,                         // JSON was "6734" (String) → Integer
                    /* prefixIATA           */ "HV",
                    /* prefixICAO           */ "TRA",
                    /* mainFlight           */ "HV6734",
                    /* isOperationalFlight  */ Boolean.TRUE,
                    /* flightDirection      */ "A",                          // Arrivals
                    /* lastUpdatedAt        */ OffsetDateTime.parse("2025-11-03T00:41:12.116Z"),
                    /* actualLandingTime    */ OffsetDateTime.parse("2025-11-02T23:02Z"),
                    /* estimatedLandingTime */ OffsetDateTime.parse("2025-11-02T23:02:07Z"),
                    /* expectedTimeOnBelt   */ OffsetDateTime.parse("2025-11-02T23:36:50Z"),
                    /* scheduleDate         */ "2025-11-03",
                    /* scheduleDateTime     */ OffsetDateTime.parse("2025-11-02T23:10Z"),
                    /* scheduleTime         */ "00:10:00",
                    /* terminal             */ 1,                            // JSON was "1" (String) → Integer
                    /* serviceType          */ "J",
                    /* schemaVersion        */ "4",                          // JSON was 4 (number) → String per your record
                    /* aircraftType         */ new AircraftType("32S", "32Q"),
                    /* baggageClaim         */ new BaggageClaim(List.of("6")),
                    /* codeshares           */ new Codeshares(List.of("KL2690")),
                    /* publicFlightState    */ new PublicFlightState(List.of("ARR")),
                    /* route                */ new Route(List.of("SVQ"), "S", false)
            );

    @BeforeEach
    void setUp() {
        client = mock(SchipholClient.class);
        service = new SchipholInformationService(client);
    }

    @Test
    void getFlightsForDate_parsesInputs_andDelegates() {
        var date = LocalDate.of(2025, 11, 3);
        when(client.flights(date, Direction.ARRIVALS)).thenReturn(List.of(flight));

        var result = service.getFlightsForDate("2025-11-03", "A");
        assertThat(result).singleElement().extracting(Flight::flightName).isEqualTo("HV6734");
        verify(client).flights(date, Direction.ARRIVALS);
    }

    @Test
    void getFlightsForDate_invalidDate_throws() {
        assertThatThrownBy(() -> service.getFlightsForDate("03-11-2025", "A"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid date format");
    }

    @Test
    void getFlightsForDate_invalidDirection_throws() {
        assertThatThrownBy(() -> service.getFlightsForDate("2025-11-03", "X"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unsupported direction");
    }

    @Test
    void getFlightByNumberOrName_delegatesAndReturns() {
        when(client.flightByNumberOrName("HV6734")).thenReturn(flight);

        var result = service.getFlightByNumberOrName("HV6734");
        assertThat(result).isNotNull();
        assertThat(result.flightName()).isEqualTo("HV6734");
    }

    @Test
    void getFlightByNumberOrName_blank_throws() {
        assertThatThrownBy(() -> service.getFlightByNumberOrName("  "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be blank");
    }

    @Test
    void getFlightsByDepartureDirection_returnEmptyList() {
        var date = LocalDate.of(2025, 11, 3);
        when(client.flights(date, Direction.DEPARTURES)).thenReturn(List.of());

        var result = service.flights(date, Direction.DEPARTURES);
        assertThat(result).isEmpty();
        verify(client).flights(date, Direction.DEPARTURES);
    }
}