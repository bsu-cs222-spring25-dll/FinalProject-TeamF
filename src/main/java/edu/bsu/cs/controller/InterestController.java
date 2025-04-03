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

    public Interest createInterest(String name) {
        return interestService.createInterest(name);
    }

    public List<Interest> searchInterests(String searchText) {
        return interestService.findInterestByNameContaining(searchText);
    }


    public Optional<Interest> getInterestByName(String name) {
        return interestService.findInterestByName(name);
    }


    public Optional<Interest> getInterestById(UUID id) {
        return interestService.findById(id);
    }

    public List<Interest> getAllInterests() {
        return interestService.getAllInterests();
    }
}