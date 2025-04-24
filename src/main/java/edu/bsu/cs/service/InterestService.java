package edu.bsu.cs.service;

import edu.bsu.cs.dao.InterestDAO;
import edu.bsu.cs.model.Interest;

import java.util.List;
import java.util.Optional;

public class InterestService {

    private final InterestDAO interestDAO;

    public InterestService(InterestDAO interestDAO) {
        this.interestDAO = interestDAO;
    }

    public List<Interest> getAllInterests() {
        return interestDAO.findAll();
    }

    public Optional<Interest> findByNameIgnoreCase(String name) {
        return interestDAO.findByName(name);
    }

    public Interest save(Interest interest) {
        return interestDAO.save(interest);
    }
}
