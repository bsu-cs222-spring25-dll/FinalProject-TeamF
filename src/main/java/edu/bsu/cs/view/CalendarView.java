package edu.bsu.cs.view;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

public class CalendarView {
    private final VBox root;

    public CalendarView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        Label title = new Label("Upcoming Events");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> eventList = new ListView<>();
        eventList.getItems().addAll(
                "Team Meeting - April 20, 2025",
                "Group Hackathon - May 2, 2025",
                "Submission Deadline - May 10, 2025"
        );

        root.getChildren().addAll(title, eventList);
    }

    public Node getRoot() {
        return root;
    }
}
