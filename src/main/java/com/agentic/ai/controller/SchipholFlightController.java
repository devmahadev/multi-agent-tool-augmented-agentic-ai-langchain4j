package com.agentic.ai.controller;

import com.agentic.ai.assistant.FlightAgent;
import com.agentic.ai.model.AskRequest;
import com.agentic.ai.model.AskResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple controller that passes requests to the LLM assistant.
 * Request validation is handled by the DTO, and any errors are managed by GlobalExceptionHandler.
 */
@RestController
@RequestMapping("/api/flight/assistant")
public class SchipholFlightController {
    private final FlightAgent assistant;

    public SchipholFlightController(FlightAgent assistant) {
        this.assistant = assistant;
    }

    @PostMapping(
            path = "/ask",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AskResponse ask(@RequestBody @Valid AskRequest request) {
        String answer = assistant.answer(request.question());
        return new AskResponse(answer);
    }
}
