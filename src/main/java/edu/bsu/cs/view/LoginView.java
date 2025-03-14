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

import java.util.Objects;

public class LoginView {

    public void showLogin(Stage primaryStage) {
        // Layout login view
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        //Shows the stage
        Scene scene = new Scene(grid, 800, 600);
        primaryStage.setTitle("GroupSync");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(LoginView.class.getResource("/Login.css").toExternalForm());
        primaryStage.show();


        Text scenetitle = new Text("Welcome to GroupSync");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("User Name: ");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        Label password = new Label ("Password: ");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        grid.add(userName, 0, 1);
        grid.add(usernameField, 1, 1);
        grid.add(password, 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(loginButton, 0, 3);
        grid.add(registerButton, 1, 3);

        // login button
        loginButton.setOnAction(event -> handleLogin(usernameField.getText(), passwordField.getText()));
        // register button
        registerButton.setOnAction(event ->{
            RegistrationView register = new RegistrationView();
            register.showRegister(primaryStage);});
    }

    private void handleLogin(String username, String password) {
        // Your login logic
        System.out.println("Login attempted with username: " + username + " and password: " + password);
    }

}
