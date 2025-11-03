package com.agentic.ai.model;

import java.time.Instant;
import java.time.OffsetDateTime;


/**
 * Schiphol API response fields
 */
public record Flight(
        String id,
        String flightName,
        Integer flightNumber,
        String prefixIATA,
        String prefixICAO,
        String mainFlight,
        Boolean isOperationalFlight,
        String flightDirection, // "A" or "D"
        OffsetDateTime lastUpdatedAt,
        OffsetDateTime actualLandingTime,
        OffsetDateTime estimatedLandingTime,
        OffsetDateTime expectedTimeOnBelt,
        String scheduleDate,
        OffsetDateTime scheduleDateTime,
        String scheduleTime,
        Integer terminal,
        String serviceType,
        String schemaVersion,
        AircraftType aircraftType,
        BaggageClaim baggageClaim,
        Codeshares codeshares,
        PublicFlightState publicFlightState,
        Route route
) {
}

