package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.service.InterestService;

import java.util.List;
import java.util.Optional;

public class InterestManager {

    private final InterestService interestService;

    public InterestManager(InterestService interestService) {
        this.interestService = interestService;
    }

    public List<Interest> getAllInterests() {
        return interestService.getAllInterests();
    }

    public Interest findOrCreateInterestByName(String name) {
        Optional<Interest> existingInterest = interestService.findByNameIgnoreCase(name);
        return existingInterest.orElseGet(() -> interestService.save(new Interest(name)));
    }
}
