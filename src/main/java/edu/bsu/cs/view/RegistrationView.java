package edu.bsu.cs.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegistrationView {

    public void showRegister(Stage primaryStage) {
        // Layout for the register
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Set up the scene
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setTitle("Register");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(RegistrationView.class.getResource("/register.css").toExternalForm());
        primaryStage.show();

        Text scenetitle = new Text("Register your Account");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label email = new Label("Email:");
        TextField emailfield = new TextField();
        emailfield.setPromptText("Enter your Email");


        Label password = new Label("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Label confirmPassword = new Label("Password: ");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm your password");

        Button registerButton = new Button("Register");


        grid.add(userName, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(email, 0, 2);
        grid.add(emailfield, 1, 2);
        grid.add(password, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(confirmPassword, 0, 4);
        grid.add(confirmPasswordField, 1, 4);
        grid.add(registerButton, 0, 5);


        // Registration button action (you can add actual registration logic here)
        registerButton.setOnAction(event -> handleRegister(usernameField.getText(), passwordField.getText(), confirmPasswordField.getText()));
    }

    private void handleRegister(String username, String password, String confirmPassword) {
        // Logic for registration (validate inputs, save data, etc.)
        System.out.println("Registering user with username: " + username);
    }
}
