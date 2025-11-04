package com.agentic.ai.assistant;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

/**
 * Interface for the LLM to interact with.
 * Any Spring bean marked with @Tool (like SchipholInformationService)
 * will be automatically picked up by LangChain4j's Spring integration.
 */
@AiService
public interface FlightAgent {

    @SystemMessage("""
            You are the Real-time Schiphol Flight Agent, returning current flights at schiphol airport
            - currently you're not participating in multi-agent Gate Agents system
            """)
    String answer(@UserMessage String question);
}
