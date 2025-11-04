package com.agentic.ai.service.tools;

import com.agentic.ai.model.gateagents.GateChangeRequest;
import com.agentic.ai.model.gateagents.GateChangeResult;
import com.agentic.ai.model.gateagents.GateStatus;
import com.agentic.ai.service.client.GateOpsClient;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class GateChangeToolsService {

    private final GateOpsClient client;

    public GateChangeToolsService(GateOpsClient client) {
        this.client = client;
    }

    @Tool("Summarize the current gate status for a flightId in one line.")
    public String summarizeGateStatus(String flightId) {
        GateStatus s = client.getGateStatus(flightId);
        if (s == null) return "No gate status.";
        return "%s — Gate %s (stand %s), %s, eff %s AMS"
                .formatted(s.flightId(), s.gate(), s.stand(), s.status(), s.effectiveFrom());
    }

    @Tool("Request a gate change. Returns a short confirmation or rejection message.")
    public String requestGateChange(String flightId, String fromGate, String toGate,
                                    String effectiveFromIso, String reasonCode, String requestedBy) {
        GateChangeRequest req = new GateChangeRequest(flightId, fromGate, toGate, effectiveFromIso, reasonCode, requestedBy);
        GateChangeResult res = client.requestGateChange(req);
        return res.accepted()
                ? "Gate change accepted: #%s — %s→%s eff %s (%s)".formatted(res.requestId(), fromGate, toGate, effectiveFromIso, reasonCode)
                : "Gate change rejected: " + res.message();
    }
}
