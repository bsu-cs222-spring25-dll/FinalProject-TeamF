package edu.bsu.cs.controller;

import edu.bsu.cs.model.User;
import edu.bsu.cs.view.InterestSelectionView;
import edu.bsu.cs.view.MainView;
import edu.bsu.cs.view.RegistrationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Optional;

public class LoginViewController {
    private final UserController userController;
    private final GroupController groupController;
    private final InterestController interestController;
    private final MessageController messageController;

    public LoginViewController(UserController userController,
                               GroupController groupController,
                               InterestController interestController,
                               MessageController messageController) {
        this.userController = userController;
        this.groupController = groupController;
        this.interestController = interestController;
        this.messageController = messageController;
    }

    public Optional<User> login(String username, String password) {
        return userController.login(username, password);
    }

    public void showRegistrationView(Stage stage) {
        RegistrationView registrationView = new RegistrationView(
                userController,
                groupController,
                interestController,
                messageController
        );
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
            MainView mainView = new MainView(
                    user,
                    userController,
                    groupController,
                    interestController,
                    messageController
            );
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

    public void showInterestSelectionView(Stage stage, User user) {
        InterestSelectionView interestView = new InterestSelectionView(
                user,
                userController,
                interestController,
                groupController,
                messageController,
                this
        );
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