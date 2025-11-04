package com.agentic.ai.controller;

import com.agentic.ai.service.client.LocalGateOpsStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.annotation.Resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LocalGateOpsController.class)
@Import(LocalGateOpsStore.class)
class LocalPassengerBySeatControllerTest {

    @Resource MockMvc mvc;

    @Test
    void passengerBySeat_found_returns_OK() throws Exception {
        mvc.perform(get("/local/passenger-by-seat")
                    .param("flightId", "HV6734-2025-11-03")
                    .param("seat", "12A"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.seat").value("12A"))
           .andExpect(jsonPath("$.checkedIn").value(true))
           .andExpect(jsonPath("$.boarded").value(false));
    }

    @Test
    void passengerBySeat_missing_returns404() throws Exception {
        mvc.perform(get("/local/passenger-by-seat")
                    .param("flightId", "HV6734-2025-11-03")
                    .param("seat", "99Z"))
           .andExpect(status().isNotFound());
    }
}
