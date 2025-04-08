package edu.bsu.cs.view;

import edu.bsu.cs.controller.LoginViewController;
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

import java.util.Optional;

public class LoginView {
    protected final LoginViewController controller;
    protected final VBox root;

    protected TextField usernameField;
    protected PasswordField passwordField;

    public LoginView(LoginViewController controller) {
        this.controller = controller;
        this.root = createLoginView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createLoginView() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Welcome to GroupSync");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label password = new Label("Password: ");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("button-primary");

        Button registerButton = new Button("Register");
        registerButton.getStyleClass().add("button-secondary");

        grid.add(userName, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(password, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 0, 3);
        grid.add(registerButton, 1, 3);

        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));
        registerButton.setOnAction(event -> {
            Stage stage = (Stage) registerButton.getScene().getWindow();
            controller.showRegistrationView(stage);
        });

        VBox vbox = new VBox(grid);
        vbox.setAlignment(Pos.CENTER);

        return vbox;
    }

    public void showLogin(Stage primaryStage) {
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("GroupSync");

        try {
            scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password");
            return;
        }

        Optional<User> userOpt = controller.login(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            showAlert("Success", "Welcome, " + user.getUsername() + "!");
            Stage stage = (Stage) root.getScene().getWindow();
            controller.showMainView(stage, user);
        } else {
            showAlert("Error", "Invalid username or password");
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
