package com.agentic.ai.service.client;

import com.agentic.ai.exception.SchipholApiException;
import com.agentic.ai.model.Direction;
import com.agentic.ai.model.Flight;
import com.agentic.ai.model.FlightsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class SchipholClientRest implements SchipholClient {

    private static final Logger logger = LoggerFactory.getLogger(SchipholClientRest.class);
    private final RestTemplate restTemplate;
    private final String BASE_URL = "https://api.schiphol.nl/public-flights";

    public SchipholClientRest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * NOTE: Fetch all today's flights with given Direction from Schiphol API
//     */
//    @Override
//    public List<Flight> flights(LocalDate scheduleDate, Direction flightDirection) {
//        try {
//            String url = String.format("%s/flights?flightDirection=%s", BASE_URL, flightDirection.code());
//            ResponseEntity<FlightsResponse> response = restTemplate.getForEntity(url, FlightsResponse.class);
//            FlightsResponse body = response.getBody();
//            return (body == null || body.flights() == null) ? List.of() : body.flights();
//
//        } catch (HttpClientErrorException | HttpServerErrorException ex) {
//            int status = ex.getStatusCode().value();
//            logger.warn("Schiphol API {} on flights(date={}, direction={}): {}",
//                    status, flightDirection, ex.getResponseBodyAsString());
//            throw new SchipholApiException("Upstream Schiphol API error: " + status, status, ex);
//        } catch (RestClientException ex) {
//            logger.warn("Schiphol API error on flights(date={}, direction={}): {}", flightDirection, ex.getMessage());
//            throw new SchipholApiException("Failed to fetch flights from Schiphol API", 502, ex);
//        }
//    }
//
//    /**
//     * Simple search for a specific flight (e.g., "HV5666") within today's arrivals.
//     */
//    @Override
//    public Flight flightByNumberOrName(String flight) {
//        // For demo I filter today's arrivals
//        List<Flight> todaysArrivals = flights(LocalDate.now(), Direction.ARRIVALS);
//        return todaysArrivals.stream()
//                .filter(f -> {
//                    if (f == null) return false;
//                    return Objects.equals(String.valueOf(f.flightNumber()), flight)
//                            || flight.equalsIgnoreCase(f.flightName())
//                            || flight.equalsIgnoreCase(f.mainFlight());
//                })
//                .findFirst()
//                .orElse(null);
//    }
}

