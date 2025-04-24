package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupManager;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class GroupListView {
    private final User currentUser;
    private final GroupManager controller;
    private final BorderPane root;
    private final ListView<Group> groupListView;

    public GroupListView(User currentUser, GroupManager controller) {
        this.currentUser = currentUser;
        this.controller = controller;
        this.root = new BorderPane();
        this.groupListView = new ListView<>();

        createUI();
        loadGroups();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void createUI() {
        Label titleLabel = new Label("All Groups");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        TextField searchField = new TextField();
        searchField.setPromptText("Search groups...");
        searchField.setPrefWidth(250);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchGroups(searchField.getText()));

        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_RIGHT);

        HBox topBox = new HBox(10);
        topBox.getChildren().addAll(titleLabel, new Pane(), searchBox);
        HBox.setHgrow(topBox.getChildren().get(1), Priority.ALWAYS);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.CENTER_LEFT);

        groupListView.setCellFactory(param -> new GroupListCell());
        VBox.setVgrow(groupListView, Priority.ALWAYS);

        root.setTop(topBox);
        root.setCenter(groupListView);
        root.setPadding(new Insets(0));
    }

    private void loadGroups() {
        List<Group> groups = controller.getAllGroups();
        ObservableList<Group> observableGroups = FXCollections.observableArrayList(groups);
        groupListView.setItems(observableGroups);
    }

    private void searchGroups(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadGroups();
            return;
        }

        List<Group> searchResults = controller.searchGroups(query);
        ObservableList<Group> observableResults = FXCollections.observableArrayList(searchResults);
        groupListView.setItems(observableResults);
    }

    private class GroupListCell extends ListCell<Group> {
        private final VBox content;
        private final Label nameLabel;
        private final Label descriptionLabel;
        private final Label membersLabel;
        private final Button joinButton;

        public GroupListCell() {
            nameLabel = new Label();
            nameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

            descriptionLabel = new Label();
            descriptionLabel.setWrapText(true);
            descriptionLabel.setMaxWidth(500);

            membersLabel = new Label();

            joinButton = new Button("Join Group");
            joinButton.setOnAction(e -> handleJoinGroup(getItem()));

            HBox buttonBox = new HBox(10, joinButton);
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
                joinButton.setDisable(isMember);
                joinButton.setText(isMember ? "Already Joined" : "Join Group");

                setGraphic(content);
            }
        }

        private void handleJoinGroup(Group group) {
            boolean success = controller.joinGroup(group, currentUser);

            if (success) {
                joinButton.setDisable(true);
                joinButton.setText("Already Joined");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("You have successfully joined " + group.getName());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Could not join the group. Please try again later.");
                alert.showAndWait();
            }
        }
    }
}
