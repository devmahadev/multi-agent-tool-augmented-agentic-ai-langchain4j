package com.agentic.ai.service.client;

import com.agentic.ai.model.gateagents.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Objects;

@Component
public class GateOpsClientRest implements GateOpsClient {

    private final RestTemplate restTemplate;
    private final String BASE_URL = "http://localhost:8080/local";

    public GateOpsClientRest(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate);
    }

    @Override
    public GateStatus getGateStatus(String flightId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/gate-status")
                .queryParam("flightId", flightId)
                .build(true).toUri();
        return restTemplate.getForObject(uri, GateStatus.class);
    }

    @Override
    public BoardingProgress getBoardingProgress(String flightId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/boarding")
                .queryParam("flightId", flightId)
                .build(true).toUri();
        return restTemplate.getForObject(uri, BoardingProgress.class);
    }

    @Override
    public PassengerPage getPassengerManifest(String flightId, int page, int size) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/passengers")
                .queryParam("flightId", flightId)
                .queryParam("page", page)
                .queryParam("size", size)
                .build(true).toUri();
        return restTemplate.getForObject(uri, PassengerPage.class);
    }

    @Override
    public PassengerLite getPassengerBySeat(String flightId, String seat) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/passenger-by-seat")
                .queryParam("flightId", flightId)
                .queryParam("seat", seat)
                .build(true).toUri();
        return restTemplate.getForObject(uri, PassengerLite.class);
    }

    @Override
    public SecurityQueue getSecurityQueue(String checkpoint) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/security")
                .queryParam("checkpoint", checkpoint)
                .build(true).toUri();
        return restTemplate.getForObject(uri, SecurityQueue.class);
    }

    @Override
    public GateChangeResult requestGateChange(GateChangeRequest request) {
        URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .path("/gate-change")
                .build(true).toUri();
        return restTemplate.postForObject(uri, request, GateChangeResult.class);
    }
}

