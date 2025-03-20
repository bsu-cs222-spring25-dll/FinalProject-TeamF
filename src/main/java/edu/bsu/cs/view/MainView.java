package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.LoginViewController;
import edu.bsu.cs.controller.MessageController;
import edu.bsu.cs.model.Group;
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

import java.util.List;

public class MainView {
    private User currentUser;
    private final UserService userService;
    private final GroupService groupService;
    private final InterestService interestService;
    private final MessageService messageService;

    // Controllers
    private GroupController groupController;
    private MessageController messageController;

    private final BorderPane root;

    // View components
    public MainView(User currentUser, UserService userService, GroupService groupService,
                    InterestService interestService, MessageService messageService) {
        this.currentUser = currentUser;
        this.userService = userService;
        this.groupService = groupService;
        this.interestService = interestService;
        this.messageService = messageService;
        this.groupController = new GroupController(groupService);
        this.messageController = new MessageController(messageService);

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
        Stage stage = (Stage) root.getScene().getWindow();

        currentUser = null;

        LoginViewController loginController = new LoginViewController(
                userService, groupService, interestService, messageService);

        LoginView loginView = new LoginView(loginController);

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
        MyGroupsView myGroupsView = new MyGroupsView(currentUser, groupController);
        root.setCenter(myGroupsView.getRoot());
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
        MessageView messageView = new MessageView(currentUser, messageController, groupService);
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
