package edu.bsu.cs.view;

import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.controller.MessageController;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.GroupService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * View for displaying and sending messages within groups.
 * Uses a three-panel layout: Group list, Selected group, and Messages.
 */
public class MessageView {
    private final User currentUser;
    private final MessageController messageController;
    private final GroupController groupController;

    private Group selectedGroup;
    private final BorderPane root;
    private final ListView<Group> groupListView;
    private final ListView<Group> userGroupsListView;
    private final ListView<Message> messagesListView;
    private final TextField messageInput;

    private Timeline refreshTimeline;
    private LocalDateTime lastRefreshTime;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public MessageView(User currentUser, MessageController messageController, GroupService groupService) {
        this.currentUser = currentUser;
        this.messageController = messageController;
        this.groupController = new GroupController(groupService);

        this.root = new BorderPane();
        this.groupListView = new ListView<>();
        this.userGroupsListView = new ListView<>();
        this.messagesListView = new ListView<>();
        this.messageInput = new TextField();
        this.lastRefreshTime = LocalDateTime.now();

        createUI();
        setupAutoRefresh();
        loadUserGroups();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void createUI() {
        // Top section - Title
        Label titleLabel = new Label("Group Messages");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        BorderPane topPane = new BorderPane();
        topPane.setCenter(titleLabel);
        topPane.setPadding(new Insets(10));

        // Left column - My Groups list
        Label myGroupsLabel = new Label("My Groups");
        myGroupsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        myGroupsLabel.setPadding(new Insets(5));

        userGroupsListView.setPrefWidth(200);
        userGroupsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> handleGroupSelection(newVal));

        VBox leftPanel = new VBox(5, myGroupsLabel, userGroupsListView);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");

        // Center panel - Messages
        messagesListView.setCellFactory(param -> new MessageCell());
        VBox.setVgrow(messagesListView, Priority.ALWAYS);

        // Message input area
        messageInput.setPromptText("Type a message...");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, messageInput, sendButton);
        HBox.setHgrow(messageInput, Priority.ALWAYS);
        inputBox.setPadding(new Insets(10));

        // Initially show placeholder
        showSelectGroupPlaceholder();

        VBox centerPanel = new VBox(messagesListView, inputBox);
        VBox.setVgrow(messagesListView, Priority.ALWAYS);

        // Combine all panels
        BorderPane mainContent = new BorderPane();
        mainContent.setLeft(leftPanel);
        mainContent.setCenter(centerPanel);

        // Set up the overall layout
        root.setTop(topPane);
        root.setCenter(mainContent);

        // Disable send controls initially
        messageInput.setDisable(true);
        sendButton.setDisable(true);

        // Enable controls when a group is selected
        userGroupsListView.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            boolean hasSelection = newVal != null;
            messageInput.setDisable(!hasSelection);
            sendButton.setDisable(!hasSelection);
        });
    }

    private void setupAutoRefresh() {
        refreshTimeline = new Timeline(
                new KeyFrame(Duration.seconds(30), event -> {
                    if (selectedGroup != null) {
                        refreshMessages();
                    }
                })
        );
        refreshTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTimeline.play();
    }

    private void loadUserGroups() {
        List<Group> userGroups = groupController.getUserGroups(currentUser);
        ObservableList<Group> groups = FXCollections.observableArrayList(userGroups);

        // Set a custom cell factory to display group names nicely
        userGroupsListView.setCellFactory(lv -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group group, boolean empty) {
                super.updateItem(group, empty);
                if (empty || group == null) {
                    setText(null);
                } else {
                    setText(group.getName());
                }
            }
        });

        userGroupsListView.setItems(groups);
    }

    private void handleGroupSelection(Group group) {
        selectedGroup = group;
        if (group != null) {
            refreshMessages();
        } else {
            showSelectGroupPlaceholder();
        }
    }

    private void refreshMessages() {
        if (selectedGroup == null) {
            return;
        }

        // Get messages for selected group
        List<Message> messages = messageController.getGroupMessages(selectedGroup);

        // Sort messages by time (oldest first)
        messages.sort(Comparator.comparing(Message::getSentAt));

        // Update the list view
        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messages);
        messagesListView.setItems(observableMessages);

        // Scroll to bottom to show most recent messages
        if (!messages.isEmpty()) {
            messagesListView.scrollTo(messages.size() - 1);
        }

        // Update last refresh time
        lastRefreshTime = LocalDateTime.now();
    }

    private void sendMessage() {
        if (selectedGroup == null) {
            return;
        }

        String content = messageInput.getText().trim();
        if (content.isEmpty()) {
            return;
        }

        try {
            // Send the message
            messageController.sendMessage(currentUser, selectedGroup, content);

            // Clear the input
            messageInput.clear();

            // Refresh messages
            refreshMessages();

        } catch (Exception e) {
            showAlert("Error", "Could not send message: " + e.getMessage());
        }
    }

    private void showSelectGroupPlaceholder() {
        Label label = new Label("No messages here yet.");
        label.setFont(new Font(14));

        VBox placeholder = new VBox(label);
        placeholder.setAlignment(Pos.CENTER);
        messagesListView.setPlaceholder(placeholder);
        messagesListView.setItems(FXCollections.observableArrayList(new ArrayList<>()));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Custom cell for displaying messages.
     */
    private class MessageCell extends ListCell<Message> {
        private final HBox container;
        private final VBox messageBox;
        private final Text nameText;
        private final Text contentText;
        private final Text timeText;

        public MessageCell() {
            nameText = new Text();
            nameText.setFont(Font.font("System", FontWeight.BOLD, 12));

            contentText = new Text();
            contentText.setWrappingWidth(300);

            timeText = new Text();
            timeText.setFont(new Font(10));
            timeText.setFill(Color.GRAY);

            messageBox = new VBox(2, nameText, contentText, timeText);
            messageBox.setPadding(new Insets(8));
            messageBox.setStyle("-fx-background-color: #f1f3f4; -fx-background-radius: 8;");

            container = new HBox();
            container.setPadding(new Insets(5, 10, 5, 10));
            container.getChildren().add(messageBox);
        }

        @Override
        protected void updateItem(Message message, boolean empty) {
            super.updateItem(message, empty);

            if (empty || message == null) {
                setGraphic(null);
                return;
            }

            // Set message content
            nameText.setText(message.getSender().getUsername());
            contentText.setText(message.getContent());

            // Format the time
            LocalDateTime sentTime = message.getSentAt();
            String formattedTime = formatMessageTime(sentTime);
            timeText.setText(formattedTime);

            // Determine if this is the current user's message
            boolean isCurrentUser = message.getSender().equals(currentUser);

            // Style based on sender
            if (isCurrentUser) {
                messageBox.setStyle("-fx-background-color: #2979ff; -fx-background-radius: 8;");
                nameText.setFill(Color.WHITE);
                contentText.setFill(Color.WHITE);
                timeText.setFill(Color.rgb(220, 220, 220));
                container.setAlignment(Pos.CENTER_RIGHT);
            } else {
                messageBox.setStyle("-fx-background-color: #f1f3f4; -fx-background-radius: 8;");
                nameText.setFill(Color.BLACK);
                contentText.setFill(Color.BLACK);
                timeText.setFill(Color.GRAY);
                container.setAlignment(Pos.CENTER_LEFT);
            }

            setGraphic(container);
        }

        private String formatMessageTime(LocalDateTime time) {
            LocalDateTime now = LocalDateTime.now();
            String timeStr = time.format(TIME_FORMATTER);

            // If message is from a different day, include the date
            if (time.toLocalDate().equals(now.toLocalDate())) {
                return timeStr;
            } else if (time.toLocalDate().equals(now.toLocalDate().minusDays(1))) {
                return "Yesterday " + timeStr;
            } else {
                return time.format(DATE_FORMATTER) + " " + timeStr;
            }
        }
    }

}