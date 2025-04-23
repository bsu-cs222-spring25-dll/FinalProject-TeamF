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
    private final EventController eventController;
    private final EventAttendeeController eventAttendeeController;
    private final VBox root;

    private final Map<Interest, CheckBox> interestCheckboxes = new HashMap<>();

    public InterestSelectionView(User user, UserController userController,
                                 InterestController interestController,
                                 GroupController groupController,
                                 MessageController messageController,
                                 LoginViewController loginViewController,
                                 EventController eventController,
                                 EventAttendeeController eventAttendeeController) {
        this.currentUser = user;
        this.userController = userController;
        this.interestController = interestController;
        this.groupController = groupController;
        this.messageController = messageController;
        this.loginViewController = loginViewController;
        this.eventController = eventController;
        this.eventAttendeeController = eventAttendeeController;
        this.root = createView();

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

        Text title = new Text("Select Your Interests");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));

        Text description = new Text("Choose interests to help us recommend groups for you");
        description.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));

        ScrollPane interestScrollPane = new ScrollPane(createInterestSelectionArea());

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);

        Button skipButton = new Button("Skip for Now");
        skipButton.getStyleClass().add("button-secondary");

        Button continueButton = new Button("Continue");
        continueButton.getStyleClass().add("button-primary");

        buttonBar.getChildren().addAll(skipButton, continueButton);

        mainContainer.getChildren().addAll(title, description, interestScrollPane, buttonBar);

        skipButton.setOnAction(e -> navigateToMainView());
        continueButton.setOnAction(e -> saveInterestsAndNavigate());

        return mainContainer;
    }

    private VBox createInterestSelectionArea() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        Label interestsLabel = new Label("Select Interests:");
        interestsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        FlowPane interestsPane = new FlowPane(10, 10);
        interestsPane.setPadding(new Insets(10));
        interestsPane.setPrefWrapLength(300);
        interestsPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        interestsPane.setMinHeight(150);

        interestCheckboxes.clear(); // fix for final reassignment
        List<Interest> interests = interestController.getAllInterests();
        for (Interest interest : interests) {
            CheckBox checkBox = new CheckBox(interest.getName());
            VBox interestBox = new VBox(checkBox);
            interestBox.setPadding(new Insets(10));
            interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
            interestBox.setPrefWidth(150);
            interestBox.getStyleClass().add("interest-box");
            interestsPane.getChildren().add(interestBox);
            interestCheckboxes.put(interest, checkBox);
        }

        // Custom Interest Input
        TextField newInterestField = new TextField();
        newInterestField.setPromptText("Enter a new interest");

        Button addInterestButton = new Button("Add Interest");

        addInterestButton.setOnAction(e -> {
            String newInterestName = newInterestField.getText().trim();
            if (!newInterestName.isEmpty()) {
                newInterestName = Character.toUpperCase(newInterestName.charAt(0)) + newInterestName.substring(1).toLowerCase();
                Interest newInterest = interestController.findOrCreateInterestByName(newInterestName);
                if (!interestCheckboxes.containsKey(newInterest)) {
                    CheckBox checkBox = new CheckBox(newInterest.getName());
                    VBox interestBox = new VBox(checkBox);
                    interestBox.setPadding(new Insets(10));
                    interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
                    interestBox.setPrefWidth(150);
                    interestBox.getStyleClass().add("interest-box");
                    interestsPane.getChildren().add(interestBox);
                    interestCheckboxes.put(newInterest, checkBox);
                    checkBox.setSelected(true);
                    checkBox.requestFocus(); // optional touch
                    newInterestField.clear();
                }
            }
        });

        HBox newInterestBox = new HBox(10, newInterestField, addInterestButton);
        newInterestBox.setAlignment(Pos.CENTER_LEFT);

        container.getChildren().addAll(interestsLabel, interestsPane, newInterestBox);
        return container;
    }

    private void saveInterestsAndNavigate() {
        try {
            boolean atLeastOneSelected = false;

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

            Optional<User> refreshedUserOpt = userController.findById(currentUser.getId());
            if (refreshedUserOpt.isPresent()) {
                currentUser = refreshedUserOpt.get();
            }

            if (atLeastOneSelected) {
                showAlert("Interests Saved", "Your interests have been saved successfully!");
            } else {
                showAlert("No Interests Selected", "You haven't selected any interests. " +
                        "This may limit the groups we can recommend for you.");
            }

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

            Optional<User> refreshedUserOpt = userController.findById(currentUser.getId());
            User refreshedUser = refreshedUserOpt.orElse(currentUser);

            MainView mainView = new MainView(refreshedUser, userController, groupController,
                    interestController, messageController, eventController, eventAttendeeController);
            Scene scene = new Scene(mainView.getRoot(), 1024, 768);

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
