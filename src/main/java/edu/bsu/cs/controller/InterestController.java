package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.service.InterestService;

import java.util.List;
import java.util.Optional;

public class InterestController {
    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    public List<Interest> getAllInterests() {
        return interestService.getAllInterests();
    }

    public Interest findOrCreateInterestByName(String name) {
        // lookup
        Optional<Interest> existing = interestService.findByNameIgnoreCase(name);
        // if not found, create & save
        return existing.orElseGet(() -> {
            Interest newInterest = new Interest(name);
            return interestService.save(newInterest);
        });
    }
}
