package com.agentic.ai.service.client;

import com.agentic.ai.model.gateagents.*;

public interface GateOpsClient {
    GateStatus getGateStatus(String flightId);
    BoardingProgress getBoardingProgress(String flightId);
    PassengerPage getPassengerManifest(String flightId, int page, int size);
    PassengerLite getPassengerBySeat(String flightId, String seat);
    SecurityQueue getSecurityQueue(String checkpoint);
    GateChangeResult requestGateChange(GateChangeRequest request);
}
