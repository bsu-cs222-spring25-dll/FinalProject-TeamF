package edu.bsu.cs.dao;

import edu.bsu.cs.model.Interest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InterestDAO extends GenericDAO<Interest, UUID> {
    Optional<Interest> findByName(String name);
    List<Interest> findByNameContaining(String nameContains);
}