package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.InterestController;
import edu.bsu.cs.controller.UserController;
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
    private final GroupController groupController;
    private final InterestController interestController;
    private final UserController userController;
    private final VBox root;
    private VBox recommendationsSection;
    private FlowPane interestTags;

    // Number of recommendations to show
    private static final int RECOMMENDATION_LIMIT = 5;

    public GroupRecommendationView(User currentUser, GroupController groupController,
                                   InterestController interestController, UserController userController) {
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

        // Header with bold font
        Text header = new Text("Recommended Groups");
        header.setFont(Font.font("System", FontWeight.BOLD, 18));
        header.getStyleClass().add("recommendation-title");

        // Your Interests section
        VBox interestsSection = createInterestsSection();

        // Explanation text
        Label explanation = new Label("Based on your interests, you might like these groups:");
        explanation.getStyleClass().add("recommendation-explanation");

        // Recommendations
        this.recommendationsSection = new VBox(15);
        recommendationsSection.getStyleClass().add("recommendations-section");

        // Initial population of recommendations
        refreshRecommendations();

        // Add all components
        mainContainer.getChildren().addAll(
                header,
                interestsSection,
                explanation,
                new Separator(),
                recommendationsSection
        );

        // Wrap in ScrollPane for many recommendations
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

        // Interest dropdown for adding new interests
        ComboBox<Interest> interestComboBox = new ComboBox<>();
        // Set a cell factory to display only the interest name
        interestComboBox.setCellFactory(lv -> new ListCell<Interest>() {
            @Override
            protected void updateItem(Interest item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : item.getName());
            }
        });
        // Also set a String converter to display only the interest name in the main field
        interestComboBox.setConverter(new StringConverter<Interest>() {
            @Override
            public String toString(Interest interest) {
                return interest == null ? "" : interest.getName();
            }

            @Override
            public Interest fromString(String string) {
                return null; // Not needed for this use case
            }
        });

        // Get available interests that the user doesn't already have
        List<Interest> availableInterests = interestController.getAllInterests();
        availableInterests.removeAll(currentUser.getInterests());
        interestComboBox.getItems().addAll(availableInterests);
        interestComboBox.setPromptText("Select an interest to add");

        Button addInterestButton = new Button("Add Interest");
        addInterestButton.setOnAction(e -> {
            Interest selectedInterest = interestComboBox.getValue();
            if (selectedInterest != null) {
                // Add interest to user and save the change
                boolean added = userController.addInterest(currentUser, selectedInterest);

                if (added) {
                    // Update the UI immediately
                    refreshInterestsDisplay(interestTags);

                    // Update the recommendations to reflect the new interest
                    refreshRecommendations();

                    // Update combobox options by removing the added interest
                    interestComboBox.getItems().remove(selectedInterest);
                    interestComboBox.setValue(null);
                }
            }
        });

        interestControls.getChildren().addAll(interestComboBox, addInterestButton);

        // Interest tags display
        this.interestTags = new FlowPane(10, 10);
        interestTags.setPadding(new Insets(5));

        // Add interest tags with remove option
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
            // Clear existing recommendations
            recommendationsSection.getChildren().clear();

            // Get the user's interests
            Set<Interest> userInterests = currentUser.getInterests();

            if (userInterests.isEmpty()) {
                Label noInterests = new Label("You haven't added any interests yet.");
                noInterests.setStyle("-fx-font-style: italic;");
                recommendationsSection.getChildren().add(noInterests);
            } else {
                // Get recommended groups based on user's interests
                List<Group> recommendedGroups = groupController.findGroupsByUserInterests(currentUser, RECOMMENDATION_LIMIT);

                if (recommendedGroups.isEmpty()) {
                    Label noRecommendations = new Label("No groups found matching your interests.");
                    noRecommendations.setStyle("-fx-font-style: italic;");
                    recommendationsSection.getChildren().add(noRecommendations);
                } else {
                    for (Group group : recommendedGroups) {
                        VBox groupCard = createGroupCard(group);
                        recommendationsSection.getChildren().add(groupCard);

                        // Add separator between cards except for the last one
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

        // Group name
        Text groupName = new Text(group.getName());
        groupName.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Description
        Label description = new Label(group.getDescription());
        description.setWrapText(true);

        // Creator
        Label creator = new Label("Created by: " + group.getCreator().getUsername());
        creator.setStyle("-fx-font-style: italic;");

        // Matching interests count and names
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

        // Join button only
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button joinButton = new Button("Join Group");
        joinButton.getStyleClass().add("join-button");

        buttonBar.getChildren().add(joinButton);

        // Check if user is already a member
        boolean isMember = group.getMembers().contains(currentUser);
        if (isMember) {
            joinButton.setText("Joined");
            joinButton.setDisable(true);
        }

        // Join button event handler
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

        // Add all components to card
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