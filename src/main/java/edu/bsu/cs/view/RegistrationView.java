package edu.bsu.cs.view;

import edu.bsu.cs.controller.LoginViewController;
import edu.bsu.cs.controller.UserController;
import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.InterestController;
import edu.bsu.cs.controller.MessageController;
import edu.bsu.cs.model.User;
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

public class RegistrationView {
    protected final UserController userController;
    protected final VBox root;

    protected TextField usernameField;
    protected TextField emailField;
    protected PasswordField passwordField;
    protected PasswordField confirmPasswordField;

    // Additional controller fields for navigation to MainView
    protected final GroupController groupController;
    protected final InterestController interestController;
    protected final MessageController messageController;

    public RegistrationView(UserController userController,
                            GroupController groupController,
                            InterestController interestController,
                            MessageController messageController) {
        this.userController = userController;
        this.groupController = groupController;
        this.interestController = interestController;
        this.messageController = messageController;
        this.root = createRegistrationView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createRegistrationView() {
        // Layout for the register
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

        // Registration button action
        registerButton.setOnAction(event -> handleRegistration());

        // Back button action
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

        // Validate inputs
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
            // Register the user with simple approach
            User user = userController.register(username, email, password);

            // Show success message
            showAlert("Success", "Account created successfully!");

            // Return to login screen
            navigateToLogin();

        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    protected void navigateToLogin() {
        // Get the current stage
        Stage stage = (Stage) root.getScene().getWindow();

        // Create a LoginViewController with all necessary controllers
        LoginViewController loginController = new LoginViewController(
                userController,
                groupController,
                interestController,
                messageController
        );

        // Create login view with controller
        LoginView loginView = new LoginView(loginController);

        // Create a scene with styling
        Scene scene = new Scene(loginView.getRoot(), 800, 600);
        try {
            scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
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