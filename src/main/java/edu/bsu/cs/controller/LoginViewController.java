package edu.bsu.cs.controller;

import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
import edu.bsu.cs.view.InterestSelectionView;
import edu.bsu.cs.view.MainView;
import edu.bsu.cs.view.RegistrationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginViewController {
    private final UserService userService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final MessageService messageService;

    public LoginViewController(UserService userService, GroupService groupService,
                               InterestService interestService, MessageService messageService) {
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.messageService = messageService;
    }

    public Optional<User> login(String username, String password) {
        return userService.login(username, password);
    }

    public void showRegistrationView(Stage stage) {
        RegistrationView registrationView = new RegistrationView(userService);
        Scene scene = new Scene(registrationView.getRoot(), 800, 600);

        // Apply CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/register.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        stage.setScene(scene);
    }

    public void showMainView(Stage stage, User user) {
        // Check if user has any interests
        if (user.getInterests().isEmpty()) {
            // If no interests are set, show the interest selection view first
            showInterestSelectionView(stage, user);
        } else {
            // User already has interests, proceed directly to main view
            MainView mainView = new MainView(user, userService, groupService, interestService, messageService);
            Scene scene = new Scene(mainView.getRoot(), 1024, 768);

            // Apply CSS
            try {
                scene.getStylesheets().add(getClass().getResource("/MainView.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("CSS not found: " + e.getMessage());
            }

            stage.setScene(scene);
            stage.setTitle("GroupSync - Welcome " + user.getUsername());
        }
    }

    /**
     * Shows the interest selection view for a user.
     *
     * @param stage The current stage
     * @param user The user selecting interests
     */
    public void showInterestSelectionView(Stage stage, User user) {
        InterestSelectionView interestView = new InterestSelectionView(
                user, userService, interestService, groupService, messageService, this);
        Scene scene = new Scene(interestView.getRoot(), 800, 600);

        // Apply CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/Interest.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("GroupSync - Select Your Interests");
    }
}