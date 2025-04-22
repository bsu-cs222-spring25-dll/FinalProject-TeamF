package edu.bsu.cs.view;

import edu.bsu.cs.controller.*;
import edu.bsu.cs.model.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class MainView {
    private User currentUser;
    private final GroupController groupController;
    private final MessageController messageController;
    private final InterestController interestController;
    private final LoginViewController loginViewController;
    private final UserController userController;
    private final EventController eventController;
    private final EventAttendeeController eventAttendeeController;

    private final BorderPane root;

    public MainView(User currentUser,
                    UserController userController,
                    GroupController groupController,
                    InterestController interestController,
                    MessageController messageController,EventController eventController,EventAttendeeController eventAttendeeContoller) {
        this.currentUser = currentUser;
        this.groupController = groupController;
        this.messageController = messageController;
        this.interestController = interestController;
        this.userController = userController;
        this.eventController=eventController;
        this.eventAttendeeController=eventAttendeeContoller;

        this.loginViewController = new LoginViewController(
                userController, groupController, interestController, messageController,eventController,eventAttendeeController
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

        MenuItem logoutItem = new MenuItem("Logout");
        logoutItem.setOnAction(e -> handleLogout());
        fileMenu.getItems().addAll(new SeparatorMenuItem(), logoutItem);

        Menu viewMenu = new Menu("View");

        MenuItem profileItem = new MenuItem("My Profile");
        profileItem.setOnAction(e -> showProfile());

        MenuItem recommendationsItem = new MenuItem("Recommended Groups");
        recommendationsItem.setOnAction(e -> showRecommendations());

        viewMenu.getItems().addAll(profileItem, recommendationsItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu);

        return menuBar;
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(10));
        sidebar.setPrefWidth(200);

        Label welcomeLabel = new Label("Welcome, " + currentUser.getUsername() + "!");

        Button groupsButton = createSidebarButton("All Groups", e -> showGroupList());
        Button myGroupsButton = createSidebarButton("My Groups", e -> showMyGroups());
        Button recommendationsButton = createSidebarButton("Recommended Groups", e -> showRecommendations());
        Button createGroupButton = createSidebarButton("Create Group", e -> showCreateGroupForm());
        Button messagesButton = createSidebarButton("Messages", e -> showMessages());
        Button profileButton = createSidebarButton("My Profile", e -> showProfile());
        Button calendarButton = createSidebarButton("Events", e -> showCalendar());


        sidebar.getChildren().addAll(
                welcomeLabel,
                new Separator(),
                groupsButton,
                myGroupsButton,
                recommendationsButton,
                createGroupButton,
                messagesButton,
                calendarButton,
                new Separator(),
                profileButton
        );

        return sidebar;
    }

    private Button createSidebarButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> eventHandler) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setOnAction(eventHandler);
        return button;
    }

    private void handleLogout() {
        Stage stage = (Stage) root.getScene().getWindow();

        currentUser = null;

        LoginView loginView = new LoginView(loginViewController);

        Scene scene = new Scene(loginView.getRoot(), 800, 600);

        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Login.css")).toExternalForm());
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
        MyGroupsView myGroupsView = new MyGroupsView(currentUser, groupController, messageController);
        root.setCenter(myGroupsView.getRoot());
    }

    private void showRecommendations() {
        GroupRecommendationView recommendationView = new GroupRecommendationView(currentUser, groupController, interestController, userController);
        root.setCenter(recommendationView.getRoot());
    }

    private void showCreateGroupForm() {
        CreateGroupView createGroupView = new CreateGroupView(groupController, interestController, currentUser);
        root.setCenter(createGroupView.getRoot());
    }

    private void showMessages() {
        MessageView messageView = new MessageView(currentUser, messageController, groupController);
        root.setCenter(messageView.getRoot());
    }

    private void showProfile() {
        ProfileView profileView=new ProfileView(userController,currentUser);
        root.setCenter(profileView.getRoot());
    }

    private void showCalendar() {
        CalendarView calendarView = new CalendarView(eventController,groupController, eventAttendeeController,currentUser);
        root.setCenter(calendarView.getRoot());
    }

}
