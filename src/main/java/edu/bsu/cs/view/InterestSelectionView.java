package edu.bsu.cs.view;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
import edu.bsu.cs.controller.LoginViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class InterestSelectionView {
    private final User currentUser;
    private final UserService userService;
    private final InterestService interestService;
    private final GroupService groupService;
    private final MessageService messageService;
    private final LoginViewController loginViewController;
    private final VBox root;

    private final Map<Interest, CheckBox> interestCheckboxes = new HashMap<>();

    public InterestSelectionView(User user, UserService userService,
                                 InterestService interestService,
                                 GroupService groupService,
                                 MessageService messageService,
                                 LoginViewController loginViewController) {
        this.currentUser = user;
        this.userService = userService;
        this.interestService = interestService;
        this.groupService = groupService;
        this.messageService = messageService;
        this.loginViewController = loginViewController;
        this.root = createView();
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
        Button continueButton = new Button("Continue");

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

        // Get all available interests
        List<Interest> availableInterests = interestService.getAllInterests();

        // Current user's interests (for pre-selection)
        Set<Interest> userInterests = currentUser.getInterests();

        // Create checkboxes for each interest
        for (Interest interest : availableInterests) {
            CheckBox checkBox = new CheckBox(interest.getName());
            checkBox.setSelected(userInterests.contains(interest));

            // Create a styled container for each checkbox
            VBox interestBox = new VBox(checkBox);
            interestBox.setPadding(new Insets(10));
            interestBox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");
            interestBox.setPrefWidth(150);

            interestsPane.getChildren().add(interestBox);
            interestCheckboxes.put(interest, checkBox);
        }

        // Wrap in ScrollPane for many interests
        ScrollPane scrollPane = new ScrollPane(interestsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        return scrollPane;
    }

    private void saveInterestsAndNavigate() {
        // Save selected interests
        for (Map.Entry<Interest, CheckBox> entry : interestCheckboxes.entrySet()) {
            Interest interest = entry.getKey();
            boolean isSelected = entry.getValue().isSelected();

            if (isSelected && !currentUser.getInterests().contains(interest)) {
                userService.addInterest(currentUser, interest);
            } else if (!isSelected && currentUser.getInterests().contains(interest)) {
                userService.removeInterest(currentUser, interest);
            }
        }

        // Navigate to main view with updated interests
        navigateToMainView();
    }

    private void navigateToMainView() {
        Stage stage = (Stage) root.getScene().getWindow();
        loginViewController.showMainView(stage, currentUser);
    }
}