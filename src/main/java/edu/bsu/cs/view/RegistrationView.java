package edu.bsu.cs.view;

import edu.bsu.cs.controller.LoginViewController;
import edu.bsu.cs.manager.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class RegistrationView {
    protected final UserManager userController;
    protected final VBox root;
    protected TextField usernameField;
    protected TextField emailField;
    protected PasswordField passwordField;
    protected PasswordField confirmPasswordField;
    protected final GroupManager groupController;
    protected final InterestManager interestController;
    protected final MessageManager messageController;
    protected final EventManager eventController;
    protected final EventAttendeeManager eventAttendeeController;

    public RegistrationView(UserManager userController,
                            GroupManager groupController,
                            InterestManager interestController,
                            MessageManager messageController,
                            EventManager eventController,
                            EventAttendeeManager eventAttendeeController) {
        this.userController = userController;
        this.groupController = groupController;
        this.interestController = interestController;
        this.messageController = messageController;
        this.eventController= eventController;
        this.eventAttendeeController=eventAttendeeController;
        this.root = createRegistrationView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createRegistrationView() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Register your Account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label email = new Label("Email:");
        emailField = new TextField();
        emailField.setPromptText("Enter your Email");

        Label password = new Label("Password: ");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Label confirmPassword = new Label("Confirm Password: ");
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button-primary");

        Button backButton = new Button("Back to Login");
        backButton.getStyleClass().add("button-secondary");

        grid.add(userName, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(email, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(password, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(confirmPassword, 0, 4);
        grid.add(confirmPasswordField, 1, 4);
        grid.add(registerButton, 0, 5);
        grid.add(backButton, 1, 5);

        registerButton.setOnAction(event -> handleRegistration());
        backButton.setOnAction(event -> navigateToLogin());

        VBox vbox = new VBox(grid);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    protected void handleRegistration() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords don't match");
            return;
        }

        if (userController == null) {
            showAlert("Error", "System configuration error");
            return;
        }

        try {
            showAlert("Success", "Account created successfully!");
            navigateToLogin();
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    protected void navigateToLogin() {
        Stage stage = (Stage) root.getScene().getWindow();
        LoginViewController loginController = new LoginViewController(
                userController,
                groupController,
                interestController,
                messageController,
                eventController,
                eventAttendeeController
        );
        LoginView loginView = new LoginView(loginController);

        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Login.css")).toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        stage.setScene(scene);
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
