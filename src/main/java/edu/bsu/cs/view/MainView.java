package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.LoginViewController;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import edu.bsu.cs.service.InterestService;
import edu.bsu.cs.service.MessageService;
import edu.bsu.cs.service.UserService;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {
    private User currentUser;
    private final UserService userService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final MessageService messageService;

    // Controllers
    private GroupController groupController;

    private final BorderPane root;

    public MainView(User currentUser, UserService userService, GroupService groupService,
                    InterestService interestService, MessageService messageService) {
        this.currentUser = currentUser;
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.messageService = messageService;

        // Initialize controllers
        this.groupController = new GroupController(groupService);

        this.root = createMainView();
    }

    public BorderPane getRoot() {
        return root;
    }

    private BorderPane createMainView() {

        BorderPane borderPane = new BorderPane();

        // Create the top menu bar
        MenuBar menuBar = createMenuBar();
        borderPane.setTop(menuBar);

        // Create the left sidebar (groups list)
        VBox sidebar = createSidebar();
        borderPane.setLeft(sidebar);

        // Default center view (group list)
        GroupListView groupListView = new GroupListView(currentUser, groupController);
        borderPane.setCenter(groupListView.getRoot());

        return borderPane;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        fileMenu.getItems().addAll(settingsItem, new SeparatorMenuItem(), logoutItem);

        // View menu
        Menu viewMenu = new Menu("View");
        MenuItem groupsItem = new MenuItem("Groups");
        groupsItem.setOnAction(e -> showGroupList());
        MenuItem profileItem = new MenuItem("My Profile");
        profileItem.setOnAction(e -> showProfile());
        viewMenu.getItems().addAll(groupsItem, profileItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu);

        return menuBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);

        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");

        Button groupsButton = new Button("All Groups");
        groupsButton.setMaxWidth(Double.MAX_VALUE);
        groupsButton.setOnAction(e -> showGroupList());

        Button myGroupsButton = new Button("My Groups");
        myGroupsButton.setMaxWidth(Double.MAX_VALUE);
        myGroupsButton.setOnAction(e -> showMyGroups());

        Button createGroupButton = new Button("Create Group");
        createGroupButton.setMaxWidth(Double.MAX_VALUE);
        createGroupButton.setOnAction(e -> showCreateGroupForm());

        Button messagesButton = new Button("Messages");
        messagesButton.setMaxWidth(Double.MAX_VALUE);
        messagesButton.setOnAction(e -> showMessages());

        Button profileButton = new Button("My Profile");
        profileButton.setMaxWidth(Double.MAX_VALUE);
        profileButton.setOnAction(e -> showProfile());

        sidebar.getChildren().addAll(
                welcomeLabel,
                new Separator(),
                groupsButton,
                myGroupsButton,
                createGroupButton,
                messagesButton,
                new Separator(),
                profileButton
        );

        return sidebar;
    }

    private void handleLogout() {
        // Get the current stage
        Stage stage = (Stage) root.getScene().getWindow();

        // Clear any cached data or state
        currentUser = null;  // Reset the current user

        // Create login controller
        LoginViewController loginController = new LoginViewController(
                userService, groupService, interestService, messageService);

        // Create a completely new login view
        LoginView loginView = new LoginView(loginController);

        // Create a new scene with the login view
        Scene scene = new Scene(loginView.getRoot(), 800, 600);

        // Apply CSS
        try {
            scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        // Set the new scene to the stage
        stage.setScene(scene);
        stage.setTitle("GroupSync");

        // Optional: Force garbage collection
        System.gc();
    }

    private void showGroupList() {
        GroupListView groupListView = new GroupListView(currentUser, groupController);
        root.setCenter(groupListView.getRoot());
    }

    private void showMyGroups() {
        // Placeholder - Create a simple "Coming Soon" view
        Label label = new Label("My Groups - Coming Soon");
        label.setStyle("-fx-font-size: 24px;");
        VBox placeholder = new VBox(label);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPadding(new Insets(20));
        root.setCenter(placeholder);
    }

    private void showCreateGroupForm() {
        // Placeholder - Create a simple "Coming Soon" view
        Label label = new Label("Create Group - Coming Soon");
        label.setStyle("-fx-font-size: 24px;");
        VBox placeholder = new VBox(label);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPadding(new Insets(20));
        root.setCenter(placeholder);
    }

    private void showMessages() {
        // Placeholder - Create a simple "Coming Soon" view
        Label label = new Label("Messages - Coming Soon");
        label.setStyle("-fx-font-size: 24px;");
        VBox placeholder = new VBox(label);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPadding(new Insets(20));
        root.setCenter(placeholder);
    }

    private void showProfile() {
        // Placeholder - Create a simple "Coming Soon" view
        Label label = new Label("User Profile - Coming Soon");
        label.setStyle("-fx-font-size: 24px;");
        VBox placeholder = new VBox(label);
        placeholder.setAlignment(javafx.geometry.Pos.CENTER);
        placeholder.setPadding(new Insets(20));
        root.setCenter(placeholder);
    }
}