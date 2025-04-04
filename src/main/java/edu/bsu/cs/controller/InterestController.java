package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.service.InterestService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InterestController {
    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    public List<Interest> getAllInterests() {
        return interestService.getAllInterests();
    }

    public Optional<Interest> findById(UUID id) {
        return interestService.findById(id);
    }

    public Interest createInterest(String name) {
        return interestService.createInterest(name);
    }

    public Interest updateInterest(Interest interest) {
        return interestService.updateInterest(interest);
    }

    public void deleteInterest(Interest interest) {
        interestService.deleteInterest(interest);
    }
}