package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.InterestController;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupView {
    private final GroupController groupController;
    private final InterestController interestController;
    private final User currentUser;
    private final List<Interest> selectedInterests = new ArrayList<>();

    // Constructor
    public CreateGroupView(GroupController groupController, InterestController interestController, User currentUser) {
        this.groupController = groupController;
        this.interestController = interestController;
        this.currentUser = currentUser;
    }

    // Returns the root node for the Create Group View
    public VBox getRoot() {
        Label titleLabel = new Label("Create a New Group");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");

        TextArea groupDescriptionField = new TextArea();
        groupDescriptionField.setPromptText("Group Description");
        groupDescriptionField.setPrefRowCount(4);

        // Interest selection section
        Label interestsLabel = new Label("Select Group Interests");
        interestsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create interest selection area
        VBox interestSelectionArea = createInterestSelectionArea();

        Button createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> {
            String name = groupNameField.getText().trim();
            String description = groupDescriptionField.getText().trim();

            if (!name.isEmpty() && !description.isEmpty()) {
                createGroup(name, description, true);
            } else {
                showError("Please fill in all fields.");
            }
        });

        // Create a VBox layout and add components
        VBox formLayout = new VBox(10, titleLabel,
                groupNameField,
                groupDescriptionField,
                interestsLabel,
                interestSelectionArea,
                createGroupButton);
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER);

        return formLayout;
    }

    private VBox createInterestSelectionArea() {
        // Get all available interests using your controller
        List<Interest> availableInterests = interestController.getAllInterests();

        // Create a flow pane to hold interest checkboxes
        FlowPane interestsPane = new FlowPane(10, 10);
        interestsPane.setPadding(new Insets(10));
        interestsPane.setPrefWidth(600);

        // Create checkboxes for each interest
        for (Interest interest : availableInterests) {
            CheckBox checkBox = new CheckBox(interest.getName());

            // Add event handler for selection
            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal) {
                    selectedInterests.add(interest);
                } else {
                    selectedInterests.remove(interest);
                }
            });

            // Create a styled container for each checkbox
            VBox interestBox = new VBox(checkBox);
            interestBox.setPadding(new Insets(8));
            interestBox.setStyle("-fx-background-color:  #a0a0a0; -fx-background-radius: 5;");
            interestBox.setPrefWidth(120);

            interestsPane.getChildren().add(interestBox);
        }

        // Wrap in ScrollPane for many interests
        ScrollPane scrollPane = new ScrollPane(interestsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox interestSection = new VBox(10, scrollPane);
        interestSection.setPadding(new Insets(5));

        return interestSection;
    }

    private void createGroup(String name, String description, boolean isPublic) {
        Group group = groupController.createGroup(name, description, currentUser, isPublic);

        if (group != null) {
            // Add selected interests to the group
            boolean allSuccess = true;

            for (Interest interest : selectedInterests) {
                try {
                    boolean success = groupController.addInterestToGroup(group, interest);
                    if (!success) {
                        allSuccess = false;
                    }
                } catch (Exception e) {
                    System.err.println("Error adding interest: " + e.getMessage());
                    allSuccess = false;
                }
            }

            if (allSuccess) {
                showInfo("Group created successfully with all selected interests!");
            } else {
                showInfo("Group created, but some interests could not be added.");
            }

        } else {
            showError("Error creating the group. Please try again.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}