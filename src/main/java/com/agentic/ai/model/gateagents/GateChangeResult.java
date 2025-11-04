package com.agentic.ai.model.gateagents;

public record GateChangeResult(
        String requestId,
        boolean accepted,
        String message
) { }
