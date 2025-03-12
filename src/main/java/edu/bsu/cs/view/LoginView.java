package edu.bsu.cs.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoginView {

    public void showLogin(Stage primaryStage) {
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Layout login view
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(usernameField, 0, 0);
        grid.add(passwordField, 0, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 0, 3);

        // login button
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));

        // register button
        registerButton.setOnAction(event -> handleRegister());

        //Shows the stage
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String username, String password) {
        // Your login logic
        System.out.println("Login attempted with username: " + username + " and password: " + password);
    }

    private void handleRegister() {
        // Handle registration logic (open the registration view )
        System.out.println("Register button clicked");
    }
}
