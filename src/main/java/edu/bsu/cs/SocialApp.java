package edu.bsu.cs;

import edu.bsu.cs.dao.*;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
import edu.bsu.cs.util.DatabaseInitializer;
import edu.bsu.cs.util.HibernateUtil;
import edu.bsu.cs.view.LoginView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import java.sql.SQLException;

public class SocialApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize database
        new DatabaseInitializer().initialize();

        // Start H2 Console server
        try {
            org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console started, available at http://localhost:8082");
        } catch (SQLException e) {
            System.err.println("Failed to start H2 Console: " + e.getMessage());
            e.printStackTrace();
        }

        // Initialize DAOs
        UserDAO userDAO = new UserDAOImpl();
        GroupDAO groupDAO = new GroupDAOImpl();
        InterestDAO interestDAO = new InterestDAOImpl();
        MessageDAO messageDAO = new MessageDAOImpl();

        // Initialize Services
        UserService userService = new UserService(userDAO);
        GroupService groupService = new GroupService(); // Implement actual constructor when needed
        InterestService interestService = new InterestService(); // Implement actual constructor when needed
        MessageService messageService = new MessageService(); // Implement actual constructor when needed

        // Set up JavaFX UI with service dependencies
        LoginView loginView = new LoginView(userService);
        loginView.showLogin(primaryStage);
    }

    @Override
    public void stop() {
        // Clean up resources
        HibernateUtil.shutdown();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}