package edu.bsu.cs.view;

import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
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
    protected final UserService userService;
    protected final VBox root;

    protected TextField usernameField;
    protected TextField emailField;
    protected PasswordField passwordField;
    protected PasswordField confirmPasswordField;

    // Additional service fields for navigation to MainView
    protected GroupService groupService;
    protected InterestService interestService;
    protected MessageService messageService;

    public RegistrationView() {
        // Default constructor for empty initialization
        this.userService = null;
        this.root = createRegistrationView();
    }

    public RegistrationView(UserService userService) {
        this.userService = userService;
        this.root = createRegistrationView();
    }

    public RegistrationView(UserService userService, GroupService groupService,
                            InterestService interestService, MessageService messageService) {
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.messageService = messageService;
        this.root = createRegistrationView();
    }

    public VBox getRoot() {
        return root;
    }

    // Getter methods for fields to make them accessible to subclasses
    protected TextField getUsernameField() {
        return usernameField;
    }

    protected TextField getEmailField() {
        return emailField;
    }

    protected PasswordField getPasswordField() {
        return passwordField;
    }

    protected PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
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
        backButton.setOnAction(event -> showLoginForm());

        VBox vbox = new VBox(grid);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public void showRegister(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Register - GroupSync");

        // Apply CSS before setting the scene
        try {
            scene.getStylesheets().add(getClass().getResource("/register.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Register CSS not found: " + e.getMessage());
            // Fallback to login CSS if register CSS is not available
            try {
                scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
            } catch (Exception ex) {
                System.err.println("Login CSS not found: " + ex.getMessage());
            }
        }

        primaryStage.setScene(scene);
        primaryStage.show();
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

        if (userService == null) {
            showAlert("Error", "System configuration error");
            return;
        }

        try {
            User user = userService.registerUser(username, email, password);
            showAlert("Success", "Account created successfully!");

            // Navigate directly to MainView if we have all the required services
            if (groupService != null && interestService != null && messageService != null) {
                showMainView(user);
            } else {
                // Otherwise, go back to the login screen
                showLoginForm();
            }
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage());
        }
    }

    /**
     * Shows the login form with proper styling.
     */
    protected void showLoginForm() {
        // Get the current stage
        Stage stage = (Stage) root.getScene().getWindow();

        // Create login view
        LoginView loginView = new LoginView(userService);

        // Important: Create a new scene with preserved styling
        Scene scene = new Scene(loginView.getRoot(), 800, 600);

        // Apply CSS to maintain styling
        try {
            scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        stage.setScene(scene);
    }

    protected void showMainView(User user) {
        try {
            // Get the current stage
            Stage stage = (Stage) root.getScene().getWindow();

            // Create main view with all required services
            MainView mainView = new MainView(user, userService, groupService, interestService, messageService);

            // Create a new scene with the main view
            Scene scene = new Scene(mainView.getRoot(), 1024, 768);

            // Try to apply a CSS for the main view if available
            try {
                scene.getStylesheets().add(getClass().getResource("/main.css").toExternalForm());
            } catch (Exception e) {
                // Fallback to login CSS if main CSS is not available
                try {
                    scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
                } catch (Exception ex) {
                    System.err.println("CSS not found: " + ex.getMessage());
                }
            }

            stage.setScene(scene);
        } catch (Exception e) {
            System.err.println("Error navigating to MainView: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Could not load main view: " + e.getMessage());
        }
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

