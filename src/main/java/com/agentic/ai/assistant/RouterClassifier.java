package com.agentic.ai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * Small, low-temp classifier that returns exactly one of:
 * BOARDING, GATE_CHANGE, SECURITY (uppercase).
 */
@AiService
public interface RouterClassifier {

    @SystemMessage("""
        Classify the user's request for airport gate operations into EXACTLY ONE of:
        - BOARDING
        - GATE_CHANGE
        - SECURITY
        Rules:
        - Return ONLY the enum token (no punctuation, no extra text).
        - BOARDING: boarding progress, groups/rows, no-shows, final call.
        - GATE_CHANGE: current gate, reassignment, effective time, reason.
        - SECURITY: queue estimates, checkpoints, holds blocking boarding.
        """)
    String classify(@UserMessage String userText);
}

