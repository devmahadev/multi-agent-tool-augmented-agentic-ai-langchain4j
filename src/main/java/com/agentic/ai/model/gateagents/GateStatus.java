package com.agentic.ai.model.gateagents;

import java.time.OffsetDateTime;

public record GateStatus(
        String flightId,
        String gate,
        String stand,
        OffsetDateTime effectiveFrom,
        String status
) { }

