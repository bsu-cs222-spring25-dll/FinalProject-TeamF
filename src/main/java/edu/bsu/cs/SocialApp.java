package edu.bsu.cs;

import edu.bsu.cs.controller.*;
import edu.bsu.cs.dao.*;
import edu.bsu.cs.service.*;
import edu.bsu.cs.util.DatabaseInitializer;
import edu.bsu.cs.util.HibernateUtil;
import edu.bsu.cs.view.LoginView;
import javafx.application.Application;
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
        GroupService groupService = new GroupService(groupDAO);
        InterestService interestService = new InterestService(interestDAO);
        MessageService messageService = new MessageService(messageDAO);

        // Initialize Controllers
        UserController userController = new UserController(userService);
        GroupController groupController = new GroupController(groupService);
        InterestController interestController = new InterestController(interestService);
        MessageController messageController = new MessageController(messageService);

        // Initialize LoginViewController with controllers
        LoginViewController loginController = new LoginViewController(
                userController,
                groupController,
                interestController,
                messageController);

        // Create and show LoginView with controller
        LoginView loginView = new LoginView(loginController);
        loginView.showLogin(primaryStage);
    }

    @Override
    public void stop() {
        // Clean up resources
        HibernateUtil.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}