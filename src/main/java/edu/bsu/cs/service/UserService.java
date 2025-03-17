package edu.bsu.cs.service;
import edu.bsu.cs.dao.UserDAO;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateSessionManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User registerUser(String username, String email, String password) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            //check if username already exits
            if(userDAO.findByUsername(username).isPresent()) {
                throw new IllegalArgumentException("Username is already in use");
            }
            if(userDAO.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email is already in use");
            }
            //create and save a user
            User user = new User(username, email, password);
            return userDAO.save(user);
        });
    }

    public Optional<User> login(String username, String password) {
        // Always fetch fresh data from the database
        Optional<User> userOpt = userDAO.findByUsername(username);

        if(userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User updateProfile(User user) {
        return userDAO.update(user);
    }

    public boolean addInterest(User user,Interest interest) {
        if(user.addInterest(interest)) {
            userDAO.update(user);
            return true;
        }
        return false;
    }

    public boolean removeInterest(User user, Interest interest) {
        if (user.removeInterest(interest)) {
            userDAO.update(user);
            return true;
        }
        return false;
    }

    public Optional<User> findById(UUID id) {
        return userDAO.findById(id);
    }
    public Optional<User> findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    public List<User> findByNameContaining(String nameContains) {
        return userDAO.findByNameContaining(nameContains);
    }

}
