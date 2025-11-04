package com.agentic.ai.model.gateagents;


import java.time.OffsetDateTime;

public record SecurityQueue(
        String checkpoint,
        int estimateMinutes,
        OffsetDateTime observedAt,
        String recommendation
) { }
