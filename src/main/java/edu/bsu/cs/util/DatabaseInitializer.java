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

        User systemUser = createSystemUser();
        createTestUser("beyonce", "beyonce@gmail.com", "b123");

        createPredefinedInterests();

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
                "Technology", "Programming", "AI & Machine Learning", "Cybersecurity",
                "Cryptocurrency", "Gadgets", "Science", "Space Exploration", "Astronomy",
                "Physics", "Biology", "Mathematics", "Coding Challenges",
                "Hiking", "Cycling", "Yoga", "Fitness", "Swimming", "Martial Arts",
                "Skateboarding", "Running", "Weightlifting", "Rock Climbing",
                "Tennis", "Basketball", "Soccer", "American Football", "Camping",
                "Backpacking", "Surfing", "Skiing", "Fishing", "Kayaking",
                "Photography", "Painting", "Digital Art", "Graphic Design", "Theater",
                "Fashion", "Makeup", "Interior Design", "Calligraphy", "Jewelry Design",
                "Tattoo Art", "DIY Projects", "Crafting", "Music", "Singing", "Dancing",
                "DJing", "Movies", "TV Shows", "Anime", "K-Pop", "Podcasts", "Stand-up Comedy",
                "Board Games", "Video Games", "Esports", "Streaming", "Theater Acting",
                "Cooking", "Baking", "Gardening", "Self-Improvement", "Mindfulness",
                "Meditation", "Journaling", "Public Speaking", "Finance", "Investing",
                "Entrepreneurship", "Business Strategy", "Leadership", "Personal Finance",
                "Marketing", "Networking", "Debating", "Philosophy", "Travel", "Road Trips",
                "Theme Parks", "Cultural Experiences", "Foodie Adventures", "Languages",
                "History", "Mythology", "World Religions", "Volunteering", "Activism",
                "Charity Work", "Environmentalism", "Pets", "Parenting", "Relationships",
                "Social Media", "Astrology", "Magic Tricks", "True Crime", "Conspiracy Theories",
                "Chess", "Puzzle Solving", "Brain Teasers", "Strategy Games", "Science Fiction",
                "Psychology", "Mythology", "Space Exploration"
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
