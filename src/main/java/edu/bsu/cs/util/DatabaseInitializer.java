package edu.bsu.cs.util;

import edu.bsu.cs.dao.InterestDAO;
import edu.bsu.cs.dao.InterestDAOImpl;
import edu.bsu.cs.dao.UserDAO;
import edu.bsu.cs.dao.UserDAOImpl;
import edu.bsu.cs.dao.GroupDAO;
import edu.bsu.cs.dao.GroupDAOImpl;
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

    public DatabaseInitializer() {
        this.userDAO = new UserDAOImpl();
        this.interestDAO = new InterestDAOImpl();
        this.groupDAO = new GroupDAOImpl();
    }

    public void initialize() {
        logger.info("Starting database initialization");

        // Create system user
        User systemUser = createSystemUser();

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