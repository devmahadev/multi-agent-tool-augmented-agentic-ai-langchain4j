package com.agentic.ai.facade;

import com.agentic.ai.assistant.*;
import com.agentic.ai.assistant.router.Agent;
import org.springframework.stereotype.Service;

@Service
public class GateAgentsOrchestrator {

    private final RouterClassifier classifier;
   // private final FlightAgent flightAgent;
    private final BoardingAgent boarding;
    private final GateChangeAgent gateChange;
    private final SecurityAgent security;

    public GateAgentsOrchestrator(RouterClassifier classifier,
                                  BoardingAgent boarding,
                                  GateChangeAgent gateChange,
                                  SecurityAgent security) {
        this.classifier = classifier;
       // this.flightAgent = flightAgent;
        this.boarding = boarding;
        this.gateChange = gateChange;
        this.security = security;
    }

    public String answer(String userText) {
        String label = classifier.classify(userText);
        Agent agent = Agent.valueOf(label.trim());
        return switch (agent) {
           // case FLIGHT -> flightAgent.answer(userText);   // Flight Agent not participating as it starts fetching real data from Schiphol APIs
            case BOARDING -> boarding.answer(userText);
            case GATE_CHANGE -> gateChange.answer(userText);
            case SECURITY -> security.answer(userText);
        };
    }
}

