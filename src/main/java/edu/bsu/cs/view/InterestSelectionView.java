package edu.bsu.cs.view;

import edu.bsu.cs.controller.*;
import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class InterestSelectionView {
    private User currentUser;
    private final UserController userController;
    private final InterestController interestController;
    private final GroupController groupController;
    private final MessageController messageController;
    private final LoginViewController loginViewController;
    private final VBox root;

    private final Map<Interest, CheckBox> interestCheckboxes = new HashMap<>();

    public InterestSelectionView(User user, UserController userController,
                                 InterestController interestController,
                                 GroupController groupController,
                                 MessageController messageController,
                                 LoginViewController loginViewController) {
        this.currentUser = user;
        this.userController = userController;
        this.interestController = interestController;
        this.groupController = groupController;
        this.messageController = messageController;
        this.loginViewController = loginViewController;
        this.root = createView();

        // After creating the root
        String cssPath = "/Interest.css";
        try {
            root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            System.out.println("Successfully loaded CSS: " + cssPath);
        } catch (Exception e) {
            System.err.println("Failed to load CSS " + cssPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createView() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.TOP_CENTER);

        // Title
        Text title = new Text("Select Your Interests");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));

        // Description
        Text description = new Text("Choose interests to help us recommend groups for you");
        description.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));

        // Create interest selection area
        ScrollPane interestScrollPane = createInterestSelectionArea();

        // Buttons
        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);

        Button skipButton = new Button("Skip for Now");
        skipButton.getStyleClass().add("button-secondary");

        Button continueButton = new Button("Continue");
        continueButton.getStyleClass().add("button-primary");

        buttonBar.getChildren().addAll(skipButton, continueButton);

        // Add components to main container
        mainContainer.getChildren().addAll(title, description, interestScrollPane, buttonBar);

        // Set up button actions
        skipButton.setOnAction(e -> navigateToMainView());
        continueButton.setOnAction(e -> saveInterestsAndNavigate());

        return mainContainer;
    }

    private ScrollPane createInterestSelectionArea() {
        // Create a flow pane to hold interest checkboxes
        FlowPane interestsPane = new FlowPane(15, 15);
        interestsPane.setPadding(new Insets(20));
        interestsPane.setPrefWidth(700);

        try {
            // Get all available interests
            List<Interest> availableInterests = interestController.getAllInterests();

            // Current user's interests (for pre-selection)
            Set<Interest> userInterests = currentUser.getInterests();

            // Create checkboxes for each interest
            for (Interest interest : availableInterests) {
                CheckBox checkBox = new CheckBox(interest.getName());
                checkBox.setWrapText(true); // Allow text to wrap
                checkBox.setSelected(userInterests.contains(interest));

                // Create a styled container for each checkbox
                VBox interestBox = new VBox(checkBox);
                interestBox.setPadding(new Insets(10));
                interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                interestBox.setPrefWidth(150);
                interestBox.getStyleClass().add("interest-box");

                interestsPane.getChildren().add(interestBox);
                interestCheckboxes.put(interest, checkBox);
            }

            // If no interests are available yet, show a message
            if (availableInterests.isEmpty()) {
                Label noInterestsLabel = new Label("No interests available in the system yet.");
                noInterestsLabel.setStyle("-fx-font-style: italic;");
                interestsPane.getChildren().add(noInterestsLabel);
            }
        } catch (Exception e) {
            System.err.println("Error loading interests: " + e.getMessage());
            e.printStackTrace();

            Label errorLabel = new Label("Error loading interests. Please try again.");
            errorLabel.setStyle("-fx-font-style: italic; -fx-text-fill: red;");
            interestsPane.getChildren().add(errorLabel);
        }

        // Wrap in ScrollPane for many interests
        ScrollPane scrollPane = new ScrollPane(interestsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        return scrollPane;
    }

    private void saveInterestsAndNavigate() {
        try {
            boolean atLeastOneSelected = false;

            // Save selected interests one by one
            for (Map.Entry<Interest, CheckBox> entry : interestCheckboxes.entrySet()) {
                Interest interest = entry.getKey();
                boolean isSelected = entry.getValue().isSelected();

                if (isSelected) {
                    atLeastOneSelected = true;
                    if (!currentUser.getInterests().contains(interest)) {
                        boolean success = userController.addInterest(currentUser, interest);
                        if (!success) {
                            System.err.println("Failed to add interest: " + interest.getName());
                        }
                    }
                } else if (currentUser.getInterests().contains(interest)) {
                    boolean success = userController.removeInterest(currentUser, interest);
                    if (!success) {
                        System.err.println("Failed to remove interest: " + interest.getName());
                    }
                }
            }

            // Refresh the user to get updated interests
            Optional<User> refreshedUserOpt = userController.findById(currentUser.getId());
            if (refreshedUserOpt.isPresent()) {
                currentUser = refreshedUserOpt.get();
            }

            // Show a confirmation or warning based on selection
            if (atLeastOneSelected) {
                showAlert("Interests Saved", "Your interests have been saved successfully!");
            } else {
                showAlert("No Interests Selected", "You haven't selected any interests. " +
                        "This may limit the groups we can recommend for you.");
            }

            // Navigate to main view with updated interests
            navigateToMainView();
        } catch (Exception e) {
            System.err.println("Error saving interests: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to save interests: " + e.getMessage());
        }
    }

    private void navigateToMainView() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();

            // Get a refreshed user from the database
            Optional<User> refreshedUserOpt = userController.findById(currentUser.getId());
            User refreshedUser = refreshedUserOpt.orElse(currentUser);

            MainView mainView = new MainView(refreshedUser, userController, groupController,
                    interestController, messageController);
            Scene scene = new Scene(mainView.getRoot(), 1024, 768);

            // Apply CSS
            try {
                scene.getStylesheets().add(getClass().getResource("/MainView.css").toExternalForm());
            } catch (Exception e) {
                System.err.println("CSS not found: " + e.getMessage());
            }

            stage.setScene(scene);
            stage.setTitle("GroupSync - Main");
        } catch (Exception e) {
            System.err.println("Error navigating to main view: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Failed to navigate to main view: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}