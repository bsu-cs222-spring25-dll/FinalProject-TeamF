package edu.bsu.cs.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DashboardView {

    public void showDashboard(Stage primaryStage) {
        BorderPane borderPane = new BorderPane();

        HBox topSection = new HBox(10);
        topSection.setPadding(new Insets(10));
        topSection.setAlignment(Pos.CENTER_LEFT);

        //Search
        TextField searchField = new TextField();
        searchField.setPromptText("Search...");
        Button searchButton = new Button("Search");

        //Home
        Label homeLabel = new Label("Home");

        topSection.getChildren().addAll(homeLabel, searchField, searchButton);

        //Groups
        VBox leftSection = new VBox(10);
        leftSection.setPadding(new Insets(10));
        leftSection.setAlignment(Pos.TOP_LEFT);

        Label groupsLabel = new Label("Groups");

        leftSection.getChildren().add(groupsLabel);

        // Other features
        VBox rightSection = new VBox(10);
        rightSection.setPadding(new Insets(10));
        rightSection.setAlignment(Pos.TOP_RIGHT);

        Button button1 = new Button("Feature 1");
        Button button2 = new Button("Feature 2");
        Button button3 = new Button("Feature 3");

        rightSection.getChildren().addAll(button1, button2, button3);

        borderPane.setTop(topSection);
        borderPane.setLeft(leftSection);
        borderPane.setRight(rightSection);

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle("Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
