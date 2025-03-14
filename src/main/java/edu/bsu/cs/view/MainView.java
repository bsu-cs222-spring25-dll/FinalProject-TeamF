package edu.bsu.cs.view;

import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Temporary MainView implementation for testing LoginView and RegistrationView
 */
public class MainView {
    private final User user;
    private final UserService userService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final MessageService messageService;
    private final BorderPane root;

    public MainView(User user, UserService userService, GroupService groupService,
                    InterestService interestService, MessageService messageService) {
        this.user = user;
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.messageService = messageService;
        this.root = createMainView();
    }

    public Parent getRoot() {
        return root;
    }

    private BorderPane createMainView() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        // Header with welcome message
        Label welcomeLabel = new Label("Welcome, " + user.getUsername() + "!");
        welcomeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));

        // User information section
        VBox userInfo = new VBox(10);
        userInfo.setPadding(new Insets(20));
        userInfo.getChildren().addAll(
                new Label("User ID: " + user.getId()),
                new Label("Username: " + user.getUsername()),
                new Label("Email: " + user.getEmail())
        );

        // Sidebar with navigation buttons
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(200);

        Button profileButton = new Button("View Profile");
        Button groupsButton = new Button("My Groups");
        Button findGroupsButton = new Button("Find Groups");
        Button logoutButton = new Button("Logout");

        sidebar.getChildren().addAll(
                profileButton,
                groupsButton,
                findGroupsButton,
                logoutButton
        );

        // Add logout functionality
        logoutButton.setOnAction(event -> {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            LoginView loginView = new LoginView(userService);
            stage.setScene(loginView.getRoot().getScene());
        });

        // Main content area with placeholder
        VBox mainContent = new VBox(10);
        mainContent.setPadding(new Insets(20));
        Label contentLabel = new Label("Temporary Main View for Testing");
        contentLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 16));
        Label instructionLabel = new Label("This is a placeholder main view to verify navigation from login/register screens.");

        // Status bar
        HBox statusBar = new HBox(10);
        statusBar.setPadding(new Insets(5, 10, 5, 10));
        statusBar.getChildren().add(new Label("Application is running normally."));

        // Add a message about interests
        VBox interestsSection = new VBox(10);
        Label interestsLabel = new Label("Your Interests:");
        interestsLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));

        StringBuilder interestsText = new StringBuilder();
        if (user.getInterests().isEmpty()) {
            interestsText.append("You haven't added any interests yet.");
        } else {
            user.getInterests().forEach(interest ->
                    interestsText.append("• ").append(interest.getName()).append("\n")
            );
        }

        Label interestsContent = new Label(interestsText.toString());
        interestsSection.getChildren().addAll(interestsLabel, interestsContent);

        mainContent.getChildren().addAll(
                contentLabel,
                instructionLabel,
                interestsSection
        );

        // Arrange components in the BorderPane
        mainLayout.setTop(welcomeLabel);
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(mainContent);
        mainLayout.setRight(userInfo);
        mainLayout.setBottom(statusBar);

        return mainLayout;
    }
}