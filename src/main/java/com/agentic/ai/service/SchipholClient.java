package com.agentic.ai.service;

import com.agentic.ai.model.Direction;
import com.agentic.ai.model.Flight;

import java.time.LocalDate;
import java.util.List;

public interface SchipholClient {
    List<Flight> flights(LocalDate date, Direction direction);

    Flight flightByNumberOrName(String flight);
}