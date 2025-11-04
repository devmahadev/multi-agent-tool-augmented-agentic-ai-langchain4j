package com.agentic.ai.model.gateagents;


import java.util.List;

public record PassengerPage(
        String flightId,
        int page,
        int size,
        boolean last,
        List<PassengerLite> content
) { }

