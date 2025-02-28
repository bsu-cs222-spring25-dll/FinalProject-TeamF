package edu.bsu.cs.dao;

import edu.bsu.cs.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDAO extends GenericDAO<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByNameContaining(String nameContains);
}