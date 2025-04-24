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
    private final User currentUser;
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
        loadCSS("/Interest.css");
    }

    public VBox getRoot() {
        return root;
    }

    private void loadCSS(String cssPath) {
        try {
            root.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
            System.out.println("Successfully loaded CSS: " + cssPath);
        } catch (Exception e) {
            System.err.println("Failed to load CSS " + cssPath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private VBox createView() {
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.CENTER);

        Text title = new Text("Select Your Interests");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        title.setWrappingWidth(400);

        Text description = new Text("Choose interests to help us recommend groups for you");
        description.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        description.setWrappingWidth(400);

        VBox interestSelectionArea = createInterestSelectionArea();
        ScrollPane interestScrollPane = new ScrollPane(interestSelectionArea);
        interestScrollPane.setFitToWidth(true);

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER);

        Button skipButton = createButton("Skip for Now", "button-secondary");
        Button continueButton = createButton("Continue", "button-primary");

        buttonBar.getChildren().addAll(skipButton, continueButton);

        mainContainer.getChildren().addAll(title, description, interestScrollPane, buttonBar);

        skipButton.setOnAction(e -> navigateToMainView());

        continueButton.setOnAction(e -> {
            List<Interest> selectedInterests = new ArrayList<>();
            for (Map.Entry<Interest, CheckBox> entry : interestCheckboxes.entrySet()) {
                if (entry.getValue().isSelected()) {
                    selectedInterests.add(entry.getKey());
                }
            }

            userController.updateUserInterests(currentUser, selectedInterests); // Make sure this method exists!
            navigateToMainView();
        });

        return mainContainer;
    }

    private Button createButton(String text, String styleClass) {
        Button button = new Button(text);
        button.getStyleClass().add(styleClass);
        return button;
    }

    private VBox createInterestSelectionArea() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);

        Label interestsLabel = new Label("Select Interests:");
        interestsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        FlowPane interestsPane = new FlowPane(10, 10);
        interestsPane.setPadding(new Insets(10));
        interestsPane.setPrefWrapLength(500);
        interestsPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        interestsPane.setMinHeight(150);
        interestsPane.setAlignment(Pos.TOP_LEFT);

        interestCheckboxes.clear();
        List<Interest> interests = interestController.getAllInterests();
        interests.forEach(interest -> addInterestToPane(interestsPane, interest));

        TextField newInterestField = new TextField();
        newInterestField.setPromptText("Enter a new interest");

        Button addInterestButton = new Button("Add Interest");
        addInterestButton.setOnAction(e -> addNewInterest(newInterestField, interestsPane));

        HBox newInterestBox = new HBox(10, newInterestField, addInterestButton);
        newInterestBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(interestsLabel, interestsPane, newInterestBox);
        return container;
    }

    private void addInterestToPane(FlowPane pane, Interest interest) {
        CheckBox checkBox = new CheckBox(interest.getName());
        checkBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));

        checkBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
        HBox checkBoxWithLabel = new HBox(5, checkBox);

        checkBoxWithLabel.setAlignment(Pos.CENTER_LEFT);

        VBox interestBox = new VBox(checkBoxWithLabel);
        interestBox.setPadding(new Insets(6));
        interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8;");
        interestBox.setAlignment(Pos.CENTER_LEFT);

        pane.getChildren().add(interestBox);
        interestCheckboxes.put(interest, checkBox);
    }

    private void addNewInterest(TextField newInterestField, FlowPane interestsPane) {
        String newInterestName = newInterestField.getText().trim();
        if (!newInterestName.isEmpty()) {
            String finalName = capitalizeFirstLetter(newInterestName);

            boolean exists = interestCheckboxes.keySet().stream()
                    .anyMatch(i -> i.getName().equalsIgnoreCase(finalName));
            if (!exists) {
                Interest newInterest = interestController.findOrCreateInterestByName(finalName);
                addInterestToPane(interestsPane, newInterest);
                newInterestField.clear();
            }
        }
    }

    private String capitalizeFirstLetter(String input) {
        return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
    }

    private void navigateToMainView() {
        try {
            Stage stage = (Stage) root.getScene().getWindow();
            Optional<User> refreshedUserOpt = userController.findById(currentUser.getId());
            User refreshedUser = refreshedUserOpt.orElse(currentUser);

            MainView mainView = new MainView(refreshedUser, userController, groupController,
                    interestController, messageController, eventController, eventAttendeeController);
            Scene scene = new Scene(mainView.getRoot(), 1024, 768);

            scene.getStylesheets().add(getClass().getResource("/MainView.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("GroupSync - Main");
        } catch (Exception e) {
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
