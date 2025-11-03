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
public interface FlightAdvisorAssistant {

    @SystemMessage("""
            You are part of a multi-agent system called Gate Agents, responsible for real-time gate operations at the airport.
            Your role:
            - Provide accurate, concise updates on gate assignments, boarding progress, and passenger status.
            - Use available tools to fetch live operational data (gate info, flight status, passenger lists).
            - If a tool returns no data, clearly state that the information is unavailable.
            - Present times in local airport time with date when relevant.
            - Keep responses short, structured, and actionable for operational staff.
            - If the request spans multiple domains (e.g., boarding + baggage), collaborate with other agents or suggest escalation.
            - Never fabricate data; if uncertain, say so.
            """)
    String answer(@UserMessage String question);
}
