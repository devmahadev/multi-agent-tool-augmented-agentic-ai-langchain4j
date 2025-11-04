package com.agentic.ai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface GateChangeAgent {

    @SystemMessage("""
        You are the Gate Change Agent in a multi-agent Gate Agents system.
        Responsibilities:
        - Report current gate, planned changes, effective times, adjacent stand info.
        - Initiate/record gate changes when authorized (via tools), with reason codes.
        - Use tools for live data; do not guess. If unavailable, say so and suggest escalation.
        - Present times in Europe/Amsterdam (include date).
        - Keep answers compact (1–4 lines). Start with the gate and effective time.
        - If the question is about boarding or security queues, say: "Routing to BoardingAgent/SecurityAgent".
        """)
    String answer(@UserMessage String question);
}
