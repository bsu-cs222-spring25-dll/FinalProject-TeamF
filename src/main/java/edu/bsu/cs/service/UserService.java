package edu.bsu.cs.service;

import edu.bsu.cs.dao.UserDAO;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateSessionManager;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("ALL")
public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String username, String email, String password) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            if (userDAO.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username is already in use");
            }
            if (userDAO.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email is already in use");
            }
            User user = new User(username, email, password);
            return userDAO.save(user);
        });
    }

    public Optional<User> login(String username, String password) {
        Optional<User> userOpt = userDAO.findByUsername(username);
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean addInterest(User user, Interest interest) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            User freshUser = session.get(User.class, user.getId());
            Interest freshInterest = session.get(Interest.class, interest.getId());

            if (freshUser == null || freshInterest == null) {
                throw new RuntimeException("Could not find user or interest in database");
            }

            boolean added = freshUser.getInterests().add(freshInterest);
            if (added) {
                user.getInterests().add(interest);
                session.update(freshUser);
            }
            return added;
        });
    }

    public boolean removeInterest(User user, Interest interest) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            User freshUser = session.get(User.class, user.getId());
            Interest freshInterest = session.get(Interest.class, interest.getId());

            if (freshUser == null || freshInterest == null) {
                throw new RuntimeException("Could not find user or interest in database");
            }

            boolean removed = freshUser.getInterests().remove(freshInterest);
            if (removed) {
                user.getInterests().remove(interest);
                session.update(freshUser);
            }
            return removed;
        });
    }

    public Optional<User> findById(UUID id) {
        return userDAO.findById(id);
    }
}
