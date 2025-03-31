package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class CreateGroupView {
    private final GroupController groupController;
    private final User currentUser;

    // Constructor
    public CreateGroupView(GroupController groupController, User currentUser) {
        this.groupController = groupController;
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

        CheckBox publicCheckBox = new CheckBox("Public Group");

        Button createGroupButton = new Button("Create Group");
        createGroupButton.setOnAction(e -> {
            String name = groupNameField.getText().trim();
            String description = groupDescriptionField.getText().trim();
            boolean isPublic = publicCheckBox.isSelected();

            if (!name.isEmpty() && !description.isEmpty()) {
                createGroup(name, description, isPublic);
            } else {
                showError("Please fill in all fields.");
            }
        });

        // Create a VBox layout and add components
        VBox formLayout = new VBox(10, titleLabel, groupNameField, groupDescriptionField, publicCheckBox, createGroupButton);
        formLayout.setPadding(new Insets(20));
        formLayout.setAlignment(Pos.CENTER);

        return formLayout;
    }

    private void createGroup(String name, String description, boolean isPublic) {
        Group group = groupController.createGroup(name, description, currentUser, isPublic);

        // Handle the success or failure of creating the group
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
        alert.showAndWait();
    }
}
