package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupManager;
import edu.bsu.cs.controller.InterestManager;
import edu.bsu.cs.controller.UserManager;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupRecommendationView {
    private final User currentUser;
    private final GroupManager groupController;
    private final InterestManager interestController;
    private final UserManager userController;
    private final VBox root;
    private VBox recommendationsSection;
    private FlowPane interestTags;

    private static final int RECOMMENDATION_LIMIT = 5;

    public GroupRecommendationView(User currentUser, GroupManager groupController,
                                   InterestManager interestController, UserManager userController) {
        this.currentUser = currentUser;
        this.groupController = groupController;
        this.interestController = interestController;
        this.userController = userController;
        this.root = createView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createView() {
        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");
        mainContainer.setPadding(new Insets(15));

        Text header = new Text("Recommended Groups");
        header.setFont(Font.font("System", FontWeight.BOLD, 18));
        header.getStyleClass().add("recommendation-title");

        VBox interestsSection = createInterestsSection();

        Label explanation = new Label("Based on your interests, you might like these groups:");
        explanation.getStyleClass().add("recommendation-explanation");

        this.recommendationsSection = new VBox(15);
        recommendationsSection.getStyleClass().add("recommendations-section");

        refreshRecommendations();

        mainContainer.getChildren().addAll(
                header,
                interestsSection,
                explanation,
                new Separator(),
                recommendationsSection
        );

        ScrollPane scrollPane = new ScrollPane(mainContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        VBox wrapper = new VBox(scrollPane);
        wrapper.setPrefHeight(600);

        return wrapper;
    }

    private VBox createInterestsSection() {
        VBox section = new VBox(10);
        section.getStyleClass().add("interests-section");
        section.setPadding(new Insets(0, 0, 10, 0));

        Label title = new Label("Your Interests");
        title.setFont(Font.font("System", FontWeight.BOLD, 14));
        title.getStyleClass().add("recommendation-subtitle");

        HBox interestControls = new HBox(10);
        interestControls.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Interest> interestComboBox = new ComboBox<>();
        interestComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Interest item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });

        interestComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Interest interest) {
                return interest == null ? "" : interest.getName();
            }

            @Override
            public Interest fromString(String string) {
                return null;
            }
        });

        List<Interest> availableInterests = interestController.getAllInterests();
        availableInterests.removeAll(currentUser.getInterests());
        interestComboBox.getItems().addAll(availableInterests);
        interestComboBox.setPromptText("Select an interest to add");

        Button addInterestButton = new Button("Add Interest");
        addInterestButton.setOnAction(e -> {
            Interest selectedInterest = interestComboBox.getValue();
            if (selectedInterest != null) {
                boolean added = userController.addInterest(currentUser, selectedInterest);
                if (added) {
                    refreshInterestsDisplay(interestTags);
                    refreshRecommendations();
                    interestComboBox.getItems().remove(selectedInterest);
                    interestComboBox.setValue(null);
                }
            }
        });

        interestControls.getChildren().addAll(interestComboBox, addInterestButton);

        this.interestTags = new FlowPane(10, 10);
        interestTags.setPadding(new Insets(5));

        refreshInterestsDisplay(interestTags);

        section.getChildren().addAll(title, interestTags, interestControls);
        return section;
    }

    private void refreshInterestsDisplay(FlowPane interestTags) {
        interestTags.getChildren().clear();

        Set<Interest> userInterests = currentUser.getInterests();
        if (userInterests.isEmpty()) {
            Label noInterests = new Label("You haven't added any interests yet.");
            noInterests.setStyle("-fx-font-style: italic;");
            interestTags.getChildren().add(noInterests);
        } else {
            for (Interest interest : userInterests) {
                HBox tagBox = new HBox(5);
                tagBox.setAlignment(Pos.CENTER_LEFT);

                Label tag = new Label(interest.getName());
                tag.getStyleClass().add("interest-tag");
                tag.setStyle("-fx-background-color: #6495ED; -fx-text-fill: white; -fx-padding: 5 8; -fx-background-radius: 4;");

                Button removeBtn = new Button("✕");
                removeBtn.setStyle("-fx-font-size: 8pt; -fx-padding: 2 4; -fx-background-radius: 4;");
                removeBtn.setOnAction(e -> {
                    userController.removeInterest(currentUser, interest);
                    refreshInterestsDisplay(interestTags);
                    refreshRecommendations();
                });

                tagBox.getChildren().addAll(tag, removeBtn);
                interestTags.getChildren().add(tagBox);
            }
        }
    }

    private void refreshRecommendations() {
        if (recommendationsSection != null) {
            recommendationsSection.getChildren().clear();

            Set<Interest> userInterests = currentUser.getInterests();

            if (userInterests.isEmpty()) {
                Label noInterests = new Label("You haven't added any interests yet.");
                noInterests.setStyle("-fx-font-style: italic;");
                recommendationsSection.getChildren().add(noInterests);
            } else {
                List<Group> recommendedGroups = groupController.findGroupsByUserInterests(currentUser, RECOMMENDATION_LIMIT);

                if (recommendedGroups.isEmpty()) {
                    Label noRecommendations = new Label("No groups found matching your interests.");
                    noRecommendations.setStyle("-fx-font-style: italic;");
                    recommendationsSection.getChildren().add(noRecommendations);
                } else {
                    for (Group group : recommendedGroups) {
                        VBox groupCard = createGroupCard(group);
                        recommendationsSection.getChildren().add(groupCard);

                        if (recommendedGroups.indexOf(group) < recommendedGroups.size() - 1) {
                            recommendationsSection.getChildren().add(new Separator());
                        }
                    }
                }
            }
        }
    }

    private VBox createGroupCard(Group group) {
        VBox card = new VBox(8);
        card.getStyleClass().add("group-card");
        card.setPadding(new Insets(10));

        Text groupName = new Text(group.getName());
        groupName.setFont(Font.font("System", FontWeight.BOLD, 14));

        Label description = new Label(group.getDescription());
        description.setWrapText(true);

        Label creator = new Label("Created by: " + group.getCreator().getUsername());
        creator.setStyle("-fx-font-style: italic;");

        Set<Interest> matchingInterests = group.getInterests().stream()
                .filter(i -> currentUser.getInterests().contains(i))
                .collect(Collectors.toSet());

        int matchCount = matchingInterests.size();

        String interestNames = matchingInterests.stream()
                .map(Interest::getName)
                .collect(Collectors.joining(", "));

        Label matchLabel = new Label(matchCount + " matching interest" + (matchCount != 1 ? "s" : "") +
                (matchCount > 0 ? ": " + interestNames : ""));
        matchLabel.setWrapText(true);

        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button joinButton = new Button("Join Group");
        joinButton.getStyleClass().add("join-button");

        buttonBar.getChildren().add(joinButton);

        boolean isMember = group.getMembers().contains(currentUser);
        if (isMember) {
            joinButton.setText("Joined");
            joinButton.setDisable(true);
        }

        joinButton.setOnAction(e -> {
            boolean success = groupController.joinGroup(group, currentUser);
            if (success) {
                joinButton.setText("Joined");
                joinButton.setDisable(true);
                showAlert("Success", "You have joined " + group.getName());
            } else {
                showAlert("Error", "Could not join the group. You might already be a member.");
            }
        });

        card.getChildren().addAll(groupName, description, creator, matchLabel, buttonBar);

        return card;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
