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
                // Tech & Science
                "Technology", "Programming", "AI & Machine Learning", "Cybersecurity",
                "Cryptocurrency", "Gadgets", "Science", "Space Exploration",

                // Fitness & Sports
                "Hiking", "Cycling", "Yoga", "Fitness", "Swimming", "Martial Arts",
                "Skateboarding", "Running", "Weightlifting", "Rock Climbing",
                "Tennis", "Basketball", "Soccer", "American Football",

                // Arts & Creativity
                "Photography", "Painting", "Digital Art", "Graphic Design",
                "Theater", "Fashion", "Interior Design", "Calligraphy",
                "Music", "Singing", "Dancing", "DJing", "Writing", "Poetry",

                // Entertainment & Media
                "Movies", "TV Shows", "Anime", "K-Pop", "Podcasts", "Stand-up Comedy",
                "Board Games", "Video Games", "Esports", "Streaming",

                // Lifestyle & Personal Development
                "Cooking", "Baking", "Gardening", "DIY Projects", "Self-Improvement",
                "Mindfulness", "Meditation", "Journaling", "Public Speaking",
                "Finance", "Investing", "Entrepreneurship", "Business Strategy",
                "Leadership", "Personal Finance", "Marketing",

                // Travel & Adventure
                "Travel", "Backpacking", "Camping", "Road Trips", "Theme Parks",
                "Cultural Experiences", "Foodie Adventures",

                // Social & Community
                "Volunteering", "Activism", "Charity Work", "Networking",
                "Debating", "Environmentalism", "Philosophy",

                // Special Interests
                "Pets", "Astrology", "Magic Tricks", "Chess", "Collecting",
                "Mythology", "True Crime", "Conspiracy Theories",

                // Learning & Academics
                "History", "Languages", "Psychology", "Philosophy", "Astronomy",
                "Biology", "Physics", "Mathematics", "Coding Challenges"
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