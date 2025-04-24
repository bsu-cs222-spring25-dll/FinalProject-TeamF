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
        mainContainer.setAlignment(Pos.CENTER); // Center the content in the VBox

        Text title = new Text("Select Your Interests");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        title.setWrappingWidth(400); // Set wrapping width to control text wrapping

        Text description = new Text("Choose interests to help us recommend groups for you");
        description.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        description.setWrappingWidth(400); // Set wrapping width to control text wrapping

        ScrollPane interestScrollPane = new ScrollPane(createInterestSelectionArea());
        interestScrollPane.setFitToWidth(true); // Ensures scroll pane fits within the container

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER); // Centered button bar

        Button skipButton = new Button("Skip for Now");
        skipButton.getStyleClass().add("button-secondary");

        Button continueButton = new Button("Continue");
        continueButton.getStyleClass().add("button-primary");

        buttonBar.getChildren().addAll(skipButton, continueButton);

        mainContainer.getChildren().addAll(title, description, interestScrollPane, buttonBar);

        skipButton.setOnAction(e -> navigateToMainView());
        continueButton.setOnAction(e -> createInterestSelectionArea());

        return mainContainer;
    }

    private VBox createInterestSelectionArea() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER); // Center the content within the VBox

        Label interestsLabel = new Label("Select Interests:");
        interestsLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        FlowPane interestsPane = new FlowPane(10, 10);
        interestsPane.setPadding(new Insets(10));
        interestsPane.setPrefWrapLength(500);
        interestsPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 5; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        interestsPane.setMinHeight(150);
        interestsPane.setAlignment(Pos.TOP_LEFT); // Center the flow pane

        interestCheckboxes.clear(); // fix for final reassignment
        List<Interest> interests = interestController.getAllInterests();
        for (Interest interest : interests) {
            CheckBox checkBox = new CheckBox();
            checkBox.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));

            Label label = new Label(interest.getName());
            label.setFont(Font.font("Tahoma", FontWeight.NORMAL, 12));
            label.setWrapText(true);
            label.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(label, Priority.ALWAYS);

            HBox checkBoxWithLabel = new HBox(5, checkBox, label);
            checkBoxWithLabel.setAlignment(Pos.CENTER_LEFT);
            checkBoxWithLabel.setFillHeight(true);

            VBox interestBox = new VBox(checkBoxWithLabel);
            interestBox.setPadding(new Insets(6));
            interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8;");
            interestBox.setAlignment(Pos.CENTER_LEFT);

            // Let the interestBox size dynamically
            interestBox.setMaxWidth(Region.USE_PREF_SIZE);
            interestBox.setMinWidth(Region.USE_COMPUTED_SIZE);

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
                    interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 8; -fx-padding: 8 12 8 12;");
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
        newInterestBox.setAlignment(Pos.CENTER); // Center the custom interest input box

        container.getChildren().addAll(interestsLabel, interestsPane, newInterestBox);
        return container;
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
