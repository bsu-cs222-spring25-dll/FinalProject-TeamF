package edu.bsu.cs;

import edu.bsu.cs.util.DatabaseInitializer;
import edu.bsu.cs.util.HibernateUtil;
import edu.bsu.cs.view.LoginView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SocialApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize database
        new DatabaseInitializer().initialize();

        // Set up JavaFX UI
        primaryStage.setTitle("Social Network App");
        StackPane root = new StackPane();
        root.getChildren().add(new Label("Welcome to Social Network App"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        LoginView loginView = new LoginView();
        loginView.showLogin(primaryStage);
    }

    @Override
    public void stop() {
        // Clean up resources
        HibernateUtil.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}