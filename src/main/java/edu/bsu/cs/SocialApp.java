package edu.bsu.cs;

import edu.bsu.cs.controller.LoginViewController;
import edu.bsu.cs.manager.*;
import edu.bsu.cs.dao.*;
import edu.bsu.cs.service.*;
import edu.bsu.cs.util.DatabaseInitializer;
import edu.bsu.cs.util.HibernateUtil;
import edu.bsu.cs.view.LoginView;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.SQLException;

@SuppressWarnings("ALL")
public class SocialApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        new DatabaseInitializer().initialize();

        try {
            org.h2.tools.Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("H2 Console started, available at http://localhost:8082");
        } catch (SQLException e) {
            System.err.println("Failed to start H2 Console: " + e.getMessage());
            e.printStackTrace();
        }

        UserDAO userDAO = new UserDAOImpl();
        GroupDAO groupDAO = new GroupDAOImpl();
        InterestDAO interestDAO = new InterestDAOImpl();
        MessageDAO messageDAO = new MessageDAOImpl();
        EventDAO eventDAO = new EventDAOImpl();
        EventAttendeeDAO eventAttendeeDAO = new EventAttendeeDAOImpl();

        UserService userService = new UserService(userDAO);
        GroupService groupService = new GroupService(groupDAO);
        InterestService interestService = new InterestService(interestDAO);
        MessageService messageService = new MessageService(messageDAO);
        EventService eventService = new EventService(eventDAO);
        EventAttendeeService eventAttendeeService = new EventAttendeeService(eventAttendeeDAO);

        UserManager userController = new UserManager(userService);
        GroupManager groupController = new GroupManager(groupService);
        InterestManager interestController = new InterestManager(interestService);
        MessageManager messageController = new MessageManager(messageService);
        EventManager eventController = new EventManager(eventService);
        EventAttendeeManager eventAttendeeController= new EventAttendeeManager(eventAttendeeService);

        LoginViewController loginController = new LoginViewController(
                userController,
                groupController,
                interestController,
                messageController,
                eventController,
                eventAttendeeController);

        LoginView loginView = new LoginView(loginController);
        loginView.showLogin(primaryStage);
    }

    @Override
    public void stop() {
        HibernateUtil.shutdown();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
