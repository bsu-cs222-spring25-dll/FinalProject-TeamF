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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateGroupView {
    private final GroupController groupController;
    private final InterestController interestController;
    private final User currentUser;
    private final List<Interest> selectedInterests = new ArrayList<>();

    public CreateGroupView(GroupController groupController, InterestController interestController, User currentUser) {
        this.groupController = groupController;
        this.interestController = interestController;
        this.currentUser = currentUser;
    }

    public VBox getRoot() {
        Label titleLabel = new Label("Create a New Group");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField groupNameField = new TextField();
        groupNameField.setPromptText("Group Name");

        TextArea groupDescriptionField = new TextArea();
        groupDescriptionField.setPromptText("Group Description");
        groupDescriptionField.setPrefRowCount(4);

        Label interestsLabel = new Label("Select Group Interests");
        interestsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

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
        FlowPane interestsPane = new FlowPane(10, 10);
        interestsPane.setPadding(new Insets(10));
        interestsPane.setPrefWidth(600);

        // Clear list to avoid carrying over from previous runs
        selectedInterests.clear();

        List<Interest> availableInterests = interestController.getAllInterests();

        Set<String> existingInterestNames = new HashSet<>();

        for (Interest interest : availableInterests) {
            existingInterestNames.add(interest.getName().toLowerCase());
            addInterestCheckbox(interestsPane, interest);
        }

        TextField customInterestField = new TextField();
        customInterestField.setPromptText("Type a new interest...");

        Button addCustomInterestButton = new Button("Add Interest");
        addCustomInterestButton.setOnAction(e -> {
            String typedName = customInterestField.getText().trim();
            if (!typedName.isEmpty()) {
                String normalized = typedName.toLowerCase();
                if (!existingInterestNames.contains(normalized)) {
                    Interest customInterest = interestController.findOrCreateInterestByName(capitalize(typedName));
                    addInterestCheckbox(interestsPane, customInterest);
                    existingInterestNames.add(normalized);
                    customInterestField.clear();
                }
            }
        });

        HBox customInterestInput = new HBox(10, customInterestField, addCustomInterestButton);
        customInterestInput.setAlignment(Pos.CENTER_LEFT);
        customInterestInput.setPadding(new Insets(5));

        ScrollPane scrollPane = new ScrollPane(interestsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox interestSection = new VBox(10, customInterestInput, scrollPane);
        interestSection.setPadding(new Insets(5));

        return interestSection;
    }

    private void addInterestCheckbox(FlowPane interestsPane, Interest interest) {
        CheckBox checkBox = new CheckBox(interest.getName());

        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                selectedInterests.add(interest);
            } else {
                selectedInterests.remove(interest);
            }
        });

        VBox interestBox = new VBox(checkBox);
        interestBox.setPadding(new Insets(8));
        interestBox.setStyle("-fx-background-color: #a0a0a0; -fx-background-radius: 5;");
        interestBox.setPrefWidth(120);

        interestsPane.getChildren().add(interestBox);
    }

    private String capitalize(String input) {
        if (input.isEmpty()) return input;
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    private void createGroup(String name, String description, boolean isPublic) {
        Set<Interest> interestSet = new HashSet<>(selectedInterests);
        Group group = groupController.createGroup(name, description, currentUser, isPublic, interestSet);

        if (group != null) {
            showInfo("Group created successfully!");
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
