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
        // assumes your AbstractDAO or InterestDAO already has a findAll()
        return interestDAO.findAll();
    }

    /** Case‑insensitive lookup, delegates to your DAO.findByName() */
    public Optional<Interest> findByNameIgnoreCase(String name) {
        return interestDAO.findByName(name);
    }

    /** Persists a brand‑new Interest via your AbstractDAO.save() */
    public Interest save(Interest interest) {
        return interestDAO.save(interest);
    }
}
