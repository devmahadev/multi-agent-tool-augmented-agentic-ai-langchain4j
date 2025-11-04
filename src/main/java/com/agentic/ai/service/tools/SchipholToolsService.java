package com.agentic.ai.service.tools;

import com.agentic.ai.model.Direction;
import com.agentic.ai.model.Flight;
import com.agentic.ai.service.client.SchipholClient;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Service
public class SchipholToolsService {

    private final SchipholClient schipholClient;

    public SchipholToolsService(SchipholClient schipholClient) {
        this.schipholClient = schipholClient;
    }

//    @Tool("Get all the today's flights with direction ('A' or 'D', or words like 'arrivals'/'departures'). " +
//            "Return an empty list if none found.")
//    public List<Flight> getFlightsForDate(String date, String direction) {
//        LocalDate parsedDate = parseDate(date);
//        Direction dir = Direction.fromCodeOrName(direction);
//        return flights(parsedDate, dir);
//    }
//
//    @Tool("Find a single flight by flight number or name (for example, 'HV5666'). Return null if not found.")
//    public Flight getFlightByNumberOrName(String flight) {
//        if (flight == null || flight.isBlank()) {
//            throw new IllegalArgumentException("flight must not be blank");
//        }
//        return schipholClient.flightByNumberOrName(flight.trim());
//    }
//
//    /**
//     * Fetch all flights for the given date and direction.
//     */
//    public List<Flight> flights(LocalDate date, Direction direction) {
//        Objects.requireNonNull(date, "date must not be null");
//        Objects.requireNonNull(direction, "direction must not be null");
//        return schipholClient.flights(date, direction);
//    }
//
//    private LocalDate parseDate(String date) {
//        if (date == null || date.isBlank()) {
//            throw new IllegalArgumentException("date must not be blank (expected ISO, e.g., 2025-11-03)");
//        }
//        try {
//            return LocalDate.parse(date.trim());
//        } catch (DateTimeParseException ex) {
//            throw new IllegalArgumentException("Invalid date format: " + date + " (expected ISO, e.g., 2025-11-03)");
//        }
//    }
}
