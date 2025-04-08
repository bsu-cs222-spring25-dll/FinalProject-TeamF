package edu.bsu.cs.service;

import edu.bsu.cs.dao.InterestDAO;
import edu.bsu.cs.model.Interest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InterestService {
    private final InterestDAO interestDAO;

    public InterestService(InterestDAO interestDAO) {
        this.interestDAO = interestDAO;
    }

    public List<Interest> findInterestByNameContaining(String nameContains) {
        return interestDAO.findByNameContaining(nameContains);
    }

    public Optional<Interest> findInterestByName(String name) {
        return interestDAO.findByName(name);
    }

    public Optional<Interest> findById(UUID id) {
        return interestDAO.findById(id);
    }

    public List<Interest> getAllInterests() {
        return interestDAO.findAll();
    }
}

