package com.agentic.ai.service.tools;

import com.agentic.ai.model.gateagents.BoardingProgress;
import com.agentic.ai.model.gateagents.PassengerLite;
import com.agentic.ai.model.gateagents.PassengerPage;
import com.agentic.ai.service.client.GateOpsClient;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class BoardingToolsService {

    private final GateOpsClient client;

    public BoardingToolsService(GateOpsClient client) {
        this.client = client;
    }

    @Tool("Summarize boarding progress for a given flightId in one or two lines.")
    public String summarizeBoarding(String flightId) {
        BoardingProgress p = client.getBoardingProgress(flightId);
        if (p == null) return "No boarding data.";
        int remaining = Math.max(0, p.total() - p.boarded());
        return "%s — %d/%d boarded (%s). Remaining %d. Groups: %s. Close ~%s AMS."
                .formatted(p.flightId(), p.boarded(), p.total(), p.phase(), remaining,
                        String.join(", ", p.groupsOpen()),
                        p.closesAt());
    }

    @Tool("Get a paged, masked passenger manifest. Returns counts only to save tokens.")
    public String summarizeManifestPage(String flightId, int page, int size) {
        PassengerPage pg = client.getPassengerManifest(flightId, page, size);
        if (pg == null || pg.content() == null) return "No manifest data.";
        long boarded = pg.content().stream().filter(PassengerLite::boarded).count();
        return "%s page %d/%s — %d pax (boarded %d)"
                .formatted(flightId, pg.page(), pg.last() ? "last" : "more", pg.content().size(), boarded);
    }

    @Tool("Check if a given seat (for example., '12A') for a flight is boarded. Returns a single short line.")
    public String seatStatusByFlightAndSeat(String flightId, String seat) {
        PassengerLite p = client.getPassengerBySeat(flightId, seat);
        if (p == null) return "No passenger found for seat %s on %s.".formatted(seat, flightId);
        String ci = p.checkedIn() ? "✓" : "✗";
        String bd = p.boarded()   ? "✓" : "✗";
        return "Seat %s — checked-in %s, boarded %s.".formatted(p.seat(), ci, bd);
    }
}

