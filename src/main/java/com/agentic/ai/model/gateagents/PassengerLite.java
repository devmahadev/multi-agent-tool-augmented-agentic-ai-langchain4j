package com.agentic.ai.model.gateagents;

public record PassengerLite(
        String paxId,
        String nameMasked,
        String seat,
        boolean checkedIn,
        boolean boarded
) { }
