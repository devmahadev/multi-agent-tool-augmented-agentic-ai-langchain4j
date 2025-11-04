package com.agentic.ai.controller;

import com.agentic.ai.assistant.FlightAgent;
import com.agentic.ai.exception.GlobalExceptionHandler;
import com.agentic.ai.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SchipholFlightController.class)
@Import(GlobalExceptionHandler.class)
class AskControllerTest {

    @Resource
    private MockMvc mvc;

    @Resource
    private ObjectMapper objectMapper;

    @MockitoBean
    private FlightAgent assistant;

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

    @Test
    void ask_validRequest_returnsAnswer() throws Exception {
        Mockito.when(assistant.answer(anyString()))
                .thenReturn("Your flight HV5666 arrives at 11:30 CET.");

        AskRequest req = new AskRequest("When does HV5666 arrive?");

        mvc.perform(post("/api/flight/assistant/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.answer").value("Your flight HV5666 arrives at 11:30 CET."));
    }


    @Test
    void ask_blankQuestion_returns_BadRequest() throws Exception {
        AskRequest req = new AskRequest("  ");

        mvc.perform(post("/api/flight/assistant/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.question").exists());
    }

    @Test
    void ask_assistantThrows_returns_500() throws Exception {
        Mockito.when(assistant.answer(anyString()))
                .thenThrow(new RuntimeException("boom!"));

        AskRequest req = new AskRequest("Hello KLM");

        mvc.perform(post("/api/assistant/ask")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Internal Server Error"));
    }
}