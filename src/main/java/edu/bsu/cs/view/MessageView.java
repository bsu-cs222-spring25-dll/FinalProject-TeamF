package edu.bsu.cs.view;

import edu.bsu.cs.manager.GroupManager;
import edu.bsu.cs.manager.MessageManager;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
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

public class MessageView {
    private final User currentUser;
    private final MessageManager messageManager;
    private final GroupManager groupManager;

    private Group selectedGroup;
    private final BorderPane root;
    private final ListView<Group> userGroupsListView;
    private final ListView<Message> messagesListView;
    private final TextField messageInput;

    private Timeline refreshTimeline;
    private LocalDateTime lastRefreshTime;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    public MessageView(User currentUser, MessageManager messageManager, GroupManager groupManager) {
        this.currentUser = currentUser;
        this.messageManager = messageManager;
        this.groupManager = groupManager;

        this.root = new BorderPane();
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
        Label titleLabel = new Label("Group Messages");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));

        BorderPane topPane = new BorderPane();
        topPane.setCenter(titleLabel);
        topPane.setPadding(new Insets(10));

        Label myGroupsLabel = new Label("My Groups");
        myGroupsLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        myGroupsLabel.setPadding(new Insets(5));

        userGroupsListView.setPrefWidth(200);
        userGroupsListView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> handleGroupSelection(newVal));

        VBox leftPanel = new VBox(5, myGroupsLabel, userGroupsListView);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setStyle("-fx-border-color: #cccccc; -fx-border-width: 0 1 0 0;");

        messagesListView.setCellFactory(param -> new MessageCell());
        VBox.setVgrow(messagesListView, Priority.ALWAYS);

        messageInput.setPromptText("Type a message...");
        Button sendButton = new Button("Send");
        sendButton.setDefaultButton(true);
        sendButton.setOnAction(e -> sendMessage());

        HBox inputBox = new HBox(10, messageInput, sendButton);
        HBox.setHgrow(messageInput, Priority.ALWAYS);
        inputBox.setPadding(new Insets(10));

        showSelectGroupPlaceholder();

        VBox centerPanel = new VBox(messagesListView, inputBox);
        VBox.setVgrow(messagesListView, Priority.ALWAYS);

        BorderPane mainContent = new BorderPane();
        mainContent.setLeft(leftPanel);
        mainContent.setCenter(centerPanel);

        root.setTop(topPane);
        root.setCenter(mainContent);

        messageInput.setDisable(true);
        sendButton.setDisable(true);

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
        List<Group> userGroups = groupManager.getUserGroups(currentUser);
        ObservableList<Group> groups = FXCollections.observableArrayList(userGroups);

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

        List<Message> messages = messageManager.getGroupMessages(selectedGroup);
        messages.sort(Comparator.comparing(Message::getSentAt));

        ObservableList<Message> observableMessages = FXCollections.observableArrayList(messages);
        messagesListView.setItems(observableMessages);

        if (!messages.isEmpty()) {
            messagesListView.scrollTo(messages.size() - 1);
        }

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
            messageManager.sendMessage(currentUser, selectedGroup, content);
            messageInput.clear();
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

            nameText.setText(message.getSender().getUsername());
            contentText.setText(message.getContent());

            LocalDateTime sentTime = message.getSentAt();
            String formattedTime = formatMessageTime(sentTime);
            timeText.setText(formattedTime);

            boolean isCurrentUser = message.getSender().equals(currentUser);

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