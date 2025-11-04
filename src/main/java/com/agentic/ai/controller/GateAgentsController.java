package com.agentic.ai.controller;


import com.agentic.ai.facade.GateAgentsOrchestrator;
import com.agentic.ai.model.AskRequest;
import com.agentic.ai.model.AskResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Main controller for Multi-agents Orchestration
 */
@RestController
@RequestMapping(path = "/api/gate-agents", produces = MediaType.APPLICATION_JSON_VALUE)
public class GateAgentsController {

    private final GateAgentsOrchestrator orchestrator;

    public GateAgentsController(GateAgentsOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @PostMapping(path = "/ask",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AskResponse ask(@RequestBody AskRequest req) {
        return new AskResponse(orchestrator.answer(req.question()));
    }
}
