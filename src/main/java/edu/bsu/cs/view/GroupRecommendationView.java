package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
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

import java.util.List;
import java.util.Set;

public class GroupRecommendationView {
    private final User currentUser;
    private final GroupController groupController;
    private final VBox root;

    // Number of recommendations to show
    private static final int RECOMMENDATION_LIMIT = 5;

    public GroupRecommendationView(User currentUser, GroupController groupController) {
        this.currentUser = currentUser;
        this.groupController = groupController;
        this.root = createView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createView() {
        VBox mainContainer = new VBox(20);
        mainContainer.getStyleClass().add("main-container");

        // Header
        Text header = new Text("Recommended Groups");
        header.getStyleClass().add("recommendation-title");

        // Explanation text
        Label explanation = new Label("Based on your interests, you might like these groups:");
        explanation.getStyleClass().add("recommendation-explanation");

        // Interest tags section
        VBox interestsSection = createInterestsSection();

        // Recommendations
        VBox recommendationsSection = createRecommendationsSection();
        recommendationsSection.getStyleClass().add("recommendations-section");

        // Add all components
        mainContainer.getChildren().addAll(
                header,
                explanation,
                interestsSection,
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

        Label title = new Label("Your Interests");
        title.getStyleClass().add("recommendation-subtitle");

        FlowPane interestTags = new FlowPane(10, 10);
        interestTags.setPadding(new Insets(5));

        // Add interest tags
        Set<Interest> userInterests = currentUser.getInterests();
        if (userInterests.isEmpty()) {
            Label noInterests = new Label("You haven't added any interests yet.");
            noInterests.setStyle("-fx-font-style: italic;");
            interestTags.getChildren().add(noInterests);
        } else {
            for (Interest interest : userInterests) {
                Label tag = new Label(interest.getName());
                tag.getStyleClass().add("interest-tag");
                interestTags.getChildren().add(tag);
            }
        }

        section.getChildren().addAll(title, interestTags);
        return section;
    }

    private VBox createRecommendationsSection() {
        VBox section = new VBox(15);

        // Get the user's interests
        Set<Interest> userInterests = currentUser.getInterests();

        if (userInterests.isEmpty()) {
            Label noInterests = new Label("You haven't added any interests yet.");
            noInterests.setStyle("-fx-font-style: italic;");
            section.getChildren().add(noInterests);
        } else {
            // Get recommended groups based on user's interests
            List<Group> recommendedGroups = groupController.findGroupsByUserInterests(currentUser, RECOMMENDATION_LIMIT);

            if (recommendedGroups.isEmpty()) {
                Label noRecommendations = new Label("No groups found matching your interests.");
                noRecommendations.setStyle("-fx-font-style: italic;");
                section.getChildren().add(noRecommendations);
            } else {
                for (Group group : recommendedGroups) {
                    VBox groupCard = createGroupCard(group);
                    section.getChildren().add(groupCard);
                }
            }
        }

        return section;
    }

    private VBox createGroupCard(Group group) {
        VBox card = new VBox(10);
        card.getStyleClass().add("group-card");

        // Group name
        Text groupName = new Text(group.getName());
        groupName.getStyleClass().add("group-name");

        // Description
        Label description = new Label(group.getDescription());
        description.getStyleClass().add("group-description");
        description.setWrapText(true);

        // Creator
        Label creator = new Label("Created by: " + group.getCreator().getUsername());
        creator.getStyleClass().add("group-creator");

        // Group interests
        FlowPane interestTags = new FlowPane(5, 5);
        interestTags.setPadding(new Insets(5, 0, 5, 0));

        // Matching interests count
        int matchCount = 0;

        for (Interest interest : group.getInterests()) {
            Label tag = new Label(interest.getName());

            // Highlight matching interests
            if (currentUser.getInterests().contains(interest)) {
                tag.getStyleClass().add("matching-interest-tag");
                matchCount++;
            } else {
                tag.getStyleClass().add("regular-interest-tag");
            }

            interestTags.getChildren().add(tag);
        }

        Label matchLabel = new Label(matchCount + " matching interests");
        matchLabel.getStyleClass().add("matching-count");

        // Action buttons
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button joinButton = new Button("Join Group");
        joinButton.getStyleClass().add("join-button");

        Button viewButton = new Button("View Details");
        viewButton.getStyleClass().add("details-button");

        buttonBar.getChildren().addAll(viewButton, joinButton);

        // Check if user is already a member
        boolean isMember = group.getMembers().contains(currentUser);
        if (isMember) {
            joinButton.setText("Joined");
            joinButton.setDisable(true);
        }

        // Event handlers
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

        viewButton.setOnAction(e -> {
            // Implement group details view - placeholder for now
            showAlert("Group Details", "Viewing details for " + group.getName() + " (Not implemented yet)");
        });

        // Add all components to card
        card.getChildren().addAll(groupName, description, creator, interestTags, matchLabel, buttonBar);

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