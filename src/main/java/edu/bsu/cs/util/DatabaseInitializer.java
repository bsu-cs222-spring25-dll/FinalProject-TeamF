package edu.bsu.cs.util;

import edu.bsu.cs.dao.*;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    private final UserDAO userDAO;
    private final InterestDAO interestDAO;
    private final GroupDAO groupDAO;
    private final MessageDAO messageDAO;

    public DatabaseInitializer() {
        this.userDAO = new UserDAOImpl();
        this.interestDAO = new InterestDAOImpl();
        this.groupDAO = new GroupDAOImpl();
        this.messageDAO = new MessageDAOImpl();
    }

    public void initialize() {
        logger.info("Starting database initialization");

        // Create system user and other test users
        User systemUser = createSystemUser();
        createTestUser("beyonce", "beyonce@gmail.com", "b123");
        createTestUser("nicki", "nicki@gmail.com", "n123");
        createTestUser("gracie", "gracie@gmail.com", "g123");
        createTestUser("zayn", "zayn@gmail.com", "z123");

        // Create predefined interests
        createPredefinedInterests();

        // Create predefined groups
        groupDAO.createInitialGroups(systemUser);

        logger.info("Database initialization complete");
    }

    private User createSystemUser() {
        Optional<User> existingUser = userDAO.findByUsername("system");

        if (existingUser.isPresent()) {
            logger.info("System user already exists");
            return existingUser.get();
        }

        logger.info("Creating system user");
        User systemUser = new User("system", "system@app.com", "systempassword");
        return userDAO.save(systemUser);
    }

    private User createTestUser(String username, String email, String password) {
        Optional<User> existingUser = userDAO.findByUsername(username);

        if (existingUser.isPresent()) {
            logger.info("User {} already exists", username);
            return existingUser.get();
        }

        logger.info("Creating test user: {}", username);
        User user = new User(username, email, password);
        return userDAO.save(user);
    }

    private void createPredefinedInterests() {
        List<String> interestNames = Arrays.asList(
                "Technology", "Programming", "Hiking", "Reading",
                "Music", "Movies", "Sports", "Gaming", "Cooking", "Travel"
        );

        for (String name : interestNames) {
            if (!interestDAO.findByName(name).isPresent()) {
                Interest interest = new Interest(name);
                interestDAO.save(interest);
                logger.info("Created interest: {}", name);
            }
        }
    }

    public static void main(String[] args) {
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.initialize();
        HibernateUtil.shutdown();
    }
}