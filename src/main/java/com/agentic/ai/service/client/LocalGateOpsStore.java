package com.agentic.ai.service.client;

import com.agentic.ai.model.gateagents.*;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class LocalGateOpsStore {

    // Gate status by flightId; keep latest effective entry
    private final Map<String, GateStatus> gateStatusByFlight = new ConcurrentHashMap<>();

    // Boarding progress snapshots by flightId (latest is current)
    private final Map<String, List<BoardingProgress>> boardingByFlight = new ConcurrentHashMap<>();

    // Passengers by flightId
    private final Map<String, List<PassengerLite>> paxByFlight = new ConcurrentHashMap<>();

    // Security observations by checkpoint (latest is current)
    private final Map<String, SecurityQueue> securityByCheckpoint = new ConcurrentHashMap<>();

    // Gate changes by requestId
    private final Map<String, GateChangeResult> changesByRequestId = new ConcurrentHashMap<>();

    public LocalGateOpsStore() {
        // Seed minimal data
        String flightId = "HV6734-2025-11-03";
        gateStatusByFlight.put(flightId,
                new GateStatus(flightId, "D24", "Stand 18",
                        OffsetDateTime.now().minusMinutes(30), "Gate Open"));

        boardingByFlight.put(flightId, new CopyOnWriteArrayList<>(List.of(
                new BoardingProgress(flightId, 40, 180, "Pre-boarding",
                        List.of("Families", "Group 1-2"),
                        OffsetDateTime.now().minusMinutes(25),
                        OffsetDateTime.now().plusMinutes(20)),
                new BoardingProgress(flightId, 86, 180, "Main Boarding",
                        List.of("1-3", "Rows 20-35"),
                        OffsetDateTime.now().minusMinutes(18),
                        OffsetDateTime.now().plusMinutes(12))
        )));

        paxByFlight.put(flightId, new CopyOnWriteArrayList<>(passengers));

        securityByCheckpoint.put("C",
                new SecurityQueue("C", 14, OffsetDateTime.now().minusMinutes(2),
                        "Use Checkpoint C; E is congested"));
        securityByCheckpoint.put("E",
                new SecurityQueue("E", 28, OffsetDateTime.now().minusMinutes(5),
                        "Avoid E; consider C"));
    }

    public GateStatus getGateStatus(String flightId) {
        return gateStatusByFlight.get(flightId);
    }

    public BoardingProgress getLatestBoarding(String flightId) {
        List<BoardingProgress> list = boardingByFlight.get(flightId);
        if (list == null || list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }

    public PassengerPage getManifestPage(String flightId, int page, int size) {
        List<PassengerLite> all = paxByFlight.getOrDefault(flightId, List.of());
        int from = Math.min(page * size, all.size());
        int to = Math.min(from + size, all.size());
        boolean last = to >= all.size();
        return new PassengerPage(flightId, page, size, last, all.subList(from, to));
    }

    public SecurityQueue getSecurityQueue(String checkpoint) {
        return securityByCheckpoint.get(checkpoint);
    }

    public GateChangeResult requestGateChange(GateChangeRequest req) {
        String requestId = "GC-" + UUID.randomUUID();

        // Idempotent accept: if we saw the same requestId, return success
        if (changesByRequestId.containsKey(requestId)) {
            return changesByRequestId.get(requestId);
        }

        // Update gate status immediately (or set "Pending" per SOP)
        gateStatusByFlight.put(req.flightId(),
                new GateStatus(req.flightId(), req.toGate(), null,
                        OffsetDateTime.parse(req.effectiveFromIso()),
                        "Gate Change Done"));

        GateChangeResult res = new GateChangeResult(requestId, true, "OK");
        changesByRequestId.put(requestId, res);
        return res;
    }


    public PassengerLite getPassengerBySeat(String flightId, String seat) {
        if (flightId == null || seat == null) return null;
        String target = seat.trim().toUpperCase();
        return paxByFlight.getOrDefault(flightId, List.of())
                .stream()
                .filter(p -> p != null && p.seat() != null && p.seat().trim().equalsIgnoreCase(target))
                .findFirst()
                .orElse(null);
    }

    List<PassengerLite> passengers = List.of(
            new PassengerLite("p1", "James Bond", "12A", true, false),
            new PassengerLite("p2", "Sherlock Holmes", "12B", true, true),
            new PassengerLite("p3", "Lisbeth Salander", "14C", true, false),
            new PassengerLite("p4", "Jason Bourne", "14D", false, false),
            new PassengerLite("p5", "Ethan Hunt", "15A", true, true),
            new PassengerLite("p6", "John Wick", "15B", false, false),
            new PassengerLite("p7", "Jack Ryan", "15C", true, false),
            new PassengerLite("p8", "Tony Stark", "16A", true, true),
            new PassengerLite("p9", "Bruce Wayne", "16B", false, false),
            new PassengerLite("p10", "Clark Kent", "16C", true, false),
            new PassengerLite("p11", "Peter Parker", "17A", true, true),
            new PassengerLite("p12", "Natasha Romanoff", "17B", false, false),
            new PassengerLite("p13", "Aragorn", "17C", true, false),
            new PassengerLite("p14", "Daenerys Targaryen", "18A", true, true),
            new PassengerLite("p15", "Neo", "18B", false, false),
            new PassengerLite("p16", "Luke Skywalker", "18C", true, false),
            new PassengerLite("p17", "Spock", "19A", true, true),
            new PassengerLite("p18", "Harry Potter", "19B", false, false),
            new PassengerLite("p19", "Hermione Granger", "19C", true, false),
            new PassengerLite("p20", "Ron Weasley", "20A", true, true),
            new PassengerLite("p21", "Katniss Everdeen", "20B", false, false),
            new PassengerLite("p22", "Peeta Mellark", "20C", true, false),
            new PassengerLite("p23", "Gandalf", "21A", true, true),
            new PassengerLite("p24", "Frodo Baggins", "21B", false, false),
            new PassengerLite("p25", "Samwise Gamgee", "21C", true, false),
            new PassengerLite("p26", "Legolas", "22A", true, true),
            new PassengerLite("p27", "Gimli", "22B", false, false),
            new PassengerLite("p28", "Thorin Oakenshield", "22C", true, false),
            new PassengerLite("p29", "Bilbo Baggins", "23A", true, true),
            new PassengerLite("p30", "Dumbledore", "23B", false, false),
            new PassengerLite("p31", "Severus Snape", "23C", true, false),
            new PassengerLite("p32", "Hercule Poirot", "24A", true, true),
            new PassengerLite("p33", "Miss Marple", "24B", false, false),
            new PassengerLite("p34", "Philip Marlowe", "24C", true, false),
            new PassengerLite("p35", "Fox Mulder", "25A", true, true),
            new PassengerLite("p36", "Dana Scully", "25B", false, false),
            new PassengerLite("p37", "Rick Deckard", "25C", true, false),
            new PassengerLite("p38", "Trinity", "26A", true, true),
            new PassengerLite("p39", "Morpheus", "26B", false, false),
            new PassengerLite("p40", "T-800", "26C", true, false),
            new PassengerLite("p41", "Sarah Connor", "27A", true, true),
            new PassengerLite("p42", "John Connor", "27B", false, false),
            new PassengerLite("p43", "Max Rockatansky", "27C", true, false),
            new PassengerLite("p44", "Imperator Furiosa", "28A", true, true),
            new PassengerLite("p45", "Indiana Jones", "28B", false, false),
            new PassengerLite("p46", "Lara Croft", "28C", true, false),
            new PassengerLite("p47", "James T. Kirk", "29A", true, true),
            new PassengerLite("p48", "Jean-Luc Picard", "29B", false, false),
            new PassengerLite("p49", "Data", "29C", true, false),
            new PassengerLite("p50", "Seven of Nine", "30A", true, true)
    );
}