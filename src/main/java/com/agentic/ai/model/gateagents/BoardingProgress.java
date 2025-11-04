package com.agentic.ai.model.gateagents;

import java.time.OffsetDateTime;
import java.util.List;

public record BoardingProgress(
        String flightId,
        int boarded,
        int total,
        String phase,
        List<String> groupsOpen,
        OffsetDateTime startedAt,
        OffsetDateTime closesAt
) { }
