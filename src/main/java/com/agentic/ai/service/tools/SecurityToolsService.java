package com.agentic.ai.service.tools;

import com.agentic.ai.model.gateagents.SecurityQueue;
import com.agentic.ai.service.client.GateOpsClient;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class SecurityToolsService {

    private final GateOpsClient client;

    public SecurityToolsService(GateOpsClient client) {
        this.client = client;
    }

    @Tool("Summarize security queue estimate at a checkpoint in one line.")
    public String summarizeQueue(String checkpoint) {
        SecurityQueue q = client.getSecurityQueue(checkpoint);
        if (q == null) return "No queue data.";
        return "Checkpoint %s — ~%d min (obs %s AMS). %s"
                .formatted(q.checkpoint(), q.estimateMinutes(), q.observedAt(), q.recommendation());
    }
}

