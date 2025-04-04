package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

import java.util.ArrayList;
import java.util.List;

public class MyGroupsView {
    private final User currentUser;
    private final GroupController controller;
    private final BorderPane root;
    private final ListView<Group> groupListView;
    private List<Group> groups;

    public MyGroupsView(User currentUser, GroupController controller) {
        this.currentUser = currentUser;
        this.controller = controller;
        this.root = new BorderPane();
        this.groupListView = new ListView<>();
        this.groups = new ArrayList<>();
        createUI();
        loadMyGroups();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void createUI() {
        Label titleLabel = new Label("My Groups");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        HBox topBox = new HBox(10, titleLabel);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER_LEFT);

        groupListView.setCellFactory(param -> new GroupListCell());
        VBox.setVgrow(groupListView, Priority.ALWAYS);

        root.setTop(topBox);
        root.setCenter(groupListView);
        root.setPadding(new Insets(0));
    }

    public void loadMyGroups() {
        groups = controller.getUserGroups(currentUser);

        ObservableList<Group> observableGroups = FXCollections.observableArrayList(groups);
        groupListView.setItems(observableGroups);
    }

    private class GroupListCell extends ListCell<Group> {
        private final VBox content;
        private final Label nameLabel;
        private final Label descriptionLabel;
        private final Label membersLabel;
        private final Button leaveButton;
        private final Button messageButton;

        public GroupListCell() {
            nameLabel = new Label();
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

            descriptionLabel = new Label();
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(500);

            membersLabel = new Label();

            leaveButton = new Button("Leave Group");
            leaveButton.setOnAction(e -> handleLeaveGroup(getItem()));

            messageButton = new Button("Messages");
            messageButton.setOnAction(e -> handleMessageButton(getItem()));

            HBox buttonBox = new HBox(10, leaveButton, messageButton);
            buttonBox.setPadding(new Insets(5, 0, 0, 0));

            content = new VBox(5, nameLabel, descriptionLabel, membersLabel, buttonBox);
            content.setPadding(new Insets(10));
            content.setStyle("-fx-border-color: #dee2e6; -fx-border-width: 0 0 1 0;");
        }

        @Override
        protected void updateItem(Group group, boolean empty) {
            super.updateItem(group, empty);

            if (empty || group == null) {
                setGraphic(null);
            } else {
                nameLabel.setText(group.getName());
                descriptionLabel.setText(group.getDescription());
                membersLabel.setText("Members: " + group.getMembers().size());

                boolean isMember = group.getMembers().contains(currentUser);
                leaveButton.setDisable(!isMember);
                leaveButton.setText(isMember ? "Leave Group" : "Not a member");

                // Enable/disable message button based on group membership
                messageButton.setDisable(!isMember);

                setGraphic(content);
            }
        }

        private void handleLeaveGroup(Group group) {
            boolean success = controller.leaveGroup(group, currentUser);

            if (success) {
                leaveButton.setDisable(true);
                leaveButton.setText("Left Group");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully left " + group.getName());
                alert.showAndWait();

                loadMyGroups();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not leave the group. Please try again later.");
                alert.showAndWait();
            }
        }

        private void handleMessageButton(Group group) {

        }
    }
}