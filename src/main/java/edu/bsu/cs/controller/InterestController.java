package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.service.InterestService;
import java.util.List;

public class InterestController {
    private final InterestService interestService;

    public InterestController(InterestService interestService) {
        this.interestService = interestService;
    }

    public List<Interest> getAllInterests() {
        return interestService.getAllInterests();
    }

}