package edu.bsu.cs.controller;

import edu.bsu.cs.model.User;
import edu.bsu.cs.view.InterestSelectionView;
import edu.bsu.cs.view.MainView;
import edu.bsu.cs.view.RegistrationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class LoginViewController {

    private final UserManager userController;
    private final GroupManager groupController;
    private final InterestManager interestController;
    private final MessageManager messageController;
    private final EventManager eventController;
    private final EventAttendeeManager eventAttendeeController;

    public LoginViewController(UserManager userController,
                               GroupManager groupController,
                               InterestManager interestController,
                               MessageManager messageController,
                               EventManager eventController,
                               EventAttendeeManager eventAttendeeController) {
        this.userController = userController;
        this.groupController = groupController;
        this.interestController = interestController;
        this.messageController = messageController;
        this.eventController = eventController;
        this.eventAttendeeController = eventAttendeeController;
    }

    public Optional<User> login(String username, String password) {
        return userController.login(username, password);
    }

    public void showRegistrationView(Stage stage) {
        RegistrationView registrationView = new RegistrationView(
                userController,
                groupController,
                interestController,
                messageController,
                eventController,
                eventAttendeeController
        );

        Scene scene = new Scene(registrationView.getRoot(), 800, 600);
        applyStylesheet(scene, "/register.css");

        stage.setScene(scene);
    }

    public void showMainView(Stage stage, User user) {
        if (user.getInterests().isEmpty()) {
            showInterestSelectionView(stage, user);
            return;
        }

        MainView mainView = new MainView(
                user,
                userController,
                groupController,
                interestController,
                messageController,
                eventController,
                eventAttendeeController
        );

        Scene scene = new Scene(mainView.getRoot(), 1024, 768);
        applyStylesheet(scene, "/MainView.css");

        stage.setScene(scene);
        stage.setTitle("GroupSync - Welcome " + user.getUsername());
    }

    public void showInterestSelectionView(Stage stage, User user) {
        InterestSelectionView interestView = new InterestSelectionView(
                user,
                userController,
                interestController,
                groupController,
                messageController,
                this,
                eventController,
                eventAttendeeController
        );

        Scene scene = new Scene(interestView.getRoot(), 800, 600);
        applyStylesheet(scene, "/Interest.css");

        stage.setScene(scene);
        stage.setTitle("GroupSync - Select Your Interests");
    }

    private void applyStylesheet(Scene scene, String stylesheetPath) {
        try {
            scene.getStylesheets().add(
                    Objects.requireNonNull(getClass().getResource(stylesheetPath)).toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("Stylesheet not found: " + stylesheetPath + " | " + e.getMessage());
        }
    }
}
