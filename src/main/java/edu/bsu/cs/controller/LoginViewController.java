package edu.bsu.cs.controller;

import edu.bsu.cs.manager.*;
import edu.bsu.cs.model.User;
import edu.bsu.cs.view.InterestSelectionView;
import edu.bsu.cs.view.MainView;
import edu.bsu.cs.view.RegistrationView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class LoginViewController {

    private final UserManager userManager;
    private final GroupManager groupManager;
    private final InterestManager interestManager;
    private final MessageManager messageManager;
    private final EventManager eventManager;
    private final EventAttendeeManager eventAttendeeManager;

    public LoginViewController (UserManager userManager,
                            GroupManager groupManager,
                            InterestManager interestManager,
                            MessageManager messageManager,
                            EventManager eventManager,
                            EventAttendeeManager eventAttendeeManager) {
        this.userManager = userManager;
        this.groupManager = groupManager;
        this.interestManager = interestManager;
        this.messageManager = messageManager;
        this.eventManager = eventManager;
        this.eventAttendeeManager = eventAttendeeManager;
    }

    public Optional<User> login(String username, String password) {
        return userManager.login(username, password);
    }

    public void showRegistrationView(Stage stage) {
        RegistrationView registrationView = new RegistrationView(
                userManager,
                groupManager,
                interestManager,
                messageManager,
                eventManager,
                eventAttendeeManager
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
                userManager,
                groupManager,
                interestManager,
                messageManager,
                eventManager,
                eventAttendeeManager
        );

        Scene scene = new Scene(mainView.getRoot(), 1024, 768);
        applyStylesheet(scene, "/MainView.css");

        stage.setScene(scene);
        stage.setTitle("GroupSync - Welcome " + user.getUsername());
    }

    public void showInterestSelectionView(Stage stage, User user) {
        InterestSelectionView interestView = new InterestSelectionView(
                user,
                userManager,
                interestManager,
                groupManager,
                messageManager,
                this,
                eventManager,
                eventAttendeeManager
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
