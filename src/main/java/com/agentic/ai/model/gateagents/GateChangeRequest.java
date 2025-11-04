package com.agentic.ai.model.gateagents;

public record GateChangeRequest(
        String flightId,
        String fromGate,
        String toGate,
        String effectiveFromIso,
        String reasonCode,
        String requestedBy
) {
}
