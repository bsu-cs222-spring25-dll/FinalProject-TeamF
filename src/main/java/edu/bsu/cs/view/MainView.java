package edu.bsu.cs.view;

import edu.bsu.cs.controller.*;
import edu.bsu.cs.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainView {
    private User currentUser;
    private final UserController userController;
    private final GroupController groupController;
    private final InterestController interestController;
    private final MessageController messageController;
    private final LoginViewController loginViewController;

    private final BorderPane root;

    // View components
    public MainView(User currentUser,
                    UserController userController,
                    GroupController groupController,
                    InterestController interestController,
                    MessageController messageController) {
        this.currentUser = currentUser;
        this.userController = userController;
        this.groupController = groupController;
        this.interestController = interestController;
        this.messageController = messageController;
        this.loginViewController = new LoginViewController(
                userController, groupController, interestController, messageController
        );

        this.root = createMainView();
    }

    public BorderPane getRoot() {
        return root;
    }

    private BorderPane createMainView() {
        BorderPane borderPane = new BorderPane();

        MenuBar menuBar = createMenuBar();
        borderPane.setTop(menuBar);

        VBox sidebar = createSidebar();
        borderPane.setLeft(sidebar);

        GroupListView groupListView = new GroupListView(currentUser, groupController);
        borderPane.setCenter(groupListView.getRoot());

        return borderPane;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        fileMenu.getItems().addAll(settingsItem, new SeparatorMenuItem(), logoutItem);

        Menu viewMenu = new Menu("View");
        MenuItem groupsItem = new MenuItem("Groups");
        groupsItem.setOnAction(e -> showGroupList());
        MenuItem profileItem = new MenuItem("My Profile");
        profileItem.setOnAction(e -> showProfile());

        // Add recommendation menu item
        MenuItem recommendationsItem = new MenuItem("Recommended Groups");
        recommendationsItem.setOnAction(e -> showRecommendations());

        viewMenu.getItems().addAll(groupsItem, profileItem, recommendationsItem);

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

        // Add recommendations button
        Button recommendationsButton = new Button("Recommended Groups");
        recommendationsButton.setMaxWidth(Double.MAX_VALUE);
        recommendationsButton.setOnAction(e -> showRecommendations());

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
                recommendationsButton,
                createGroupButton,
                messagesButton,
                new Separator(),
                profileButton
        );

        return sidebar;
    }

    private void handleLogout() {
        Stage stage = (Stage) root.getScene().getWindow();

        currentUser = null;

        LoginView loginView = new LoginView(loginViewController);

        Scene scene = new Scene(loginView.getRoot(), 800, 600);

        try {
            scene.getStylesheets().add(getClass().getResource("/Login.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("CSS not found: " + e.getMessage());
        }

        stage.setScene(scene);
        stage.setTitle("GroupSync");

        System.gc();
    }

    private void showGroupList() {
        GroupListView groupListView = new GroupListView(currentUser, groupController);
        root.setCenter(groupListView.getRoot());
    }

    private void showMyGroups() {
        MyGroupsView myGroupsView = new MyGroupsView(currentUser, groupController,messageController);
        root.setCenter(myGroupsView.getRoot());
    }

    // Add new method for recommendations
    private void showRecommendations() {
        GroupRecommendationView recommendationView = new GroupRecommendationView(currentUser, groupController);
        root.setCenter(recommendationView.getRoot());
    }

    private void showCreateGroupForm() {
        // Use the CreateGroupView class instead of creating UI elements here
        CreateGroupView createGroupView = new CreateGroupView(groupController, currentUser);
        root.setCenter(createGroupView.getRoot());
    }

    private void showMessages() {
        MessageView messageView = new MessageView(currentUser, messageController, groupController);
        root.setCenter(messageView.getRoot());
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