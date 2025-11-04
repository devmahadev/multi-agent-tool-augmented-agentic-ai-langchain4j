package com.agentic.ai.controller;

import com.agentic.ai.model.gateagents.*;
import com.agentic.ai.service.client.LocalGateOpsStore;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * A local stub controller:  pragmatic bridge following pattern (API stubs/fakes)
 * I had to create local endpoints as Schiphol API doesn't expose endpoints as sandbox for updating or modifying real scenarios
 * I created local endpoints that mimic /gate-status, /gate-change, etc. as a temporary stub
 */
@RestController
@RequestMapping(path = "/local", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocalGateOpsController {

    private final LocalGateOpsStore store;

    public LocalGateOpsController(LocalGateOpsStore store) {
        this.store = store;
    }

    @GetMapping("/gate-status")
    public GateStatus gateStatus(@RequestParam String flightId) {
        return store.getGateStatus(flightId);
    }

    @GetMapping("/boarding")
    public BoardingProgress boarding(@RequestParam String flightId) {
        return store.getLatestBoarding(flightId);
    }

    @GetMapping("/passengers")
    public PassengerPage passengers(@RequestParam String flightId,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "50") int size) {
        return store.getManifestPage(flightId, page, size);
    }

    @GetMapping("/security")
    public SecurityQueue security(@RequestParam String checkpoint) {
        return store.getSecurityQueue(checkpoint);
    }

    @PostMapping(path = "/gate-change", consumes = MediaType.APPLICATION_JSON_VALUE)
    public GateChangeResult gateChange(@RequestBody GateChangeRequest req) {
        return store.requestGateChange(req);
    }

    @GetMapping("/passenger-by-seat")
    public ResponseEntity<PassengerLite> passengerBySeat(@RequestParam String flightId,
                                                         @RequestParam String seat) {
        PassengerLite p = store.getPassengerBySeat(flightId, seat);
        return (p == null) ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(p);
    }
}