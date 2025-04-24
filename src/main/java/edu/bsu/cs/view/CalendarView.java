package edu.bsu.cs.view;

import edu.bsu.cs.controller.EventAttendeeController;
import edu.bsu.cs.controller.EventController;
import edu.bsu.cs.controller.GroupController;
import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateSessionManager;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ALL")
public class CalendarView {
    private final VBox root;
    private final EventController eventController;
    private final GroupController groupController;
    private final EventAttendeeController attendeeController;
    private final User currentUser;
    private ListView<Event> eventListView;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a");

    public CalendarView(EventController eventController, GroupController groupController,
                        EventAttendeeController attendeeController, User currentUser) {
        this.eventController = eventController;
        this.groupController = groupController;
        this.attendeeController = attendeeController;
        this.currentUser = currentUser;

        root = new VBox(10);
        root.setPadding(new Insets(15));

        // Create UI
        createUI();

        // Load events
        loadEvents();
    }

    private void createUI() {
        // Title
        Label title = new Label("Upcoming Events");
        title.setFont(Font.font("System", FontWeight.BOLD, 18));

        // Event list
        eventListView = new ListView<>();
        eventListView.setCellFactory(createCellFactory());

        // Add selection listener to show event details
        eventListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        showEventDetails(newValue);
                    }
                });

        // Add event button
        Button addEventButton = new Button("Add Event");
        addEventButton.setOnAction(e -> showAddEventDialog());

        // Refresh button
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> loadEvents());

        // Button container
        HBox buttonBar = new HBox(10, addEventButton, refreshButton);
        buttonBar.setPadding(new Insets(5, 0, 5, 0));

        // Add components to root
        root.getChildren().addAll(title, buttonBar, eventListView);
        VBox.setVgrow(eventListView, Priority.ALWAYS);
    }

    private void loadEvents() {
        // Clear existing items
        eventListView.getItems().clear();

        try {

            Thread loadThread = new Thread(() -> {
                try {
                    List<Event> loadedEvents = eventController.findUpcomingEventsForUser(currentUser, 20);
                    Platform.runLater(() -> {
                        eventListView.getItems().addAll(loadedEvents);

                        if (loadedEvents.isEmpty()) {
                            Label placeholder = new Label("No upcoming events");
                            placeholder.setStyle("-fx-text-fill: gray;");
                            eventListView.setPlaceholder(placeholder);
                        }
                    });
                } catch (Exception ex) {
                    Platform.runLater(() -> showAlert("Error", "Failed to load events: " + ex.getMessage()));
                }
            });

            loadThread.setDaemon(true);
            loadThread.start();
        } catch (Exception e) {
            showAlert("Error", "Failed to load events: " + e.getMessage());
        }
    }

    private Callback<ListView<Event>, ListCell<Event>> createCellFactory() {
        return listView -> new ListCell<>() {
            @Override
            protected void updateItem(Event event, boolean empty) {
                super.updateItem(event, empty);

                if (empty || event == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Create a custom cell with event details
                    VBox container = new VBox(5);
                    container.setPadding(new Insets(5, 0, 5, 0));

                    // Event title
                    Label titleLabel = new Label(event.getTitle());
                    titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

                    // Event time
                    Label timeLabel = new Label(formatEventTime(event));

                    // Group name if available
                    Label groupLabel = null;
                    if (event.getGroup() != null) {
                        groupLabel = new Label("Group: " + event.getGroup().getName());
                    }

                    // RSVP status
                    Label rsvpLabel = new Label("Loading RSVP status...");

                    // Load RSVP status asynchronously
                    Thread rsvpThread = new Thread(() -> {
                        try {
                            Optional<EventAttendee> response = attendeeController.getUserResponse(event, currentUser);

                            Platform.runLater(() -> {
                                if (response.isPresent()) {
                                    EventAttendee.AttendanceStatus status = response.get().getStatus();
                                    String statusText = "RSVP: " + status.toString();
                                    rsvpLabel.setText(statusText);

                                    // Style based on response
                                    switch (status) {
                                        case ATTENDING:
                                            rsvpLabel.setStyle("-fx-text-fill: green;");
                                            break;
                                        case MAYBE:
                                            rsvpLabel.setStyle("-fx-text-fill: orange;");
                                            break;
                                        case DECLINED:
                                            rsvpLabel.setStyle("-fx-text-fill: red;");
                                            break;
                                    }
                                } else {
                                    rsvpLabel.setText("RSVP: Not responded");
                                    rsvpLabel.setStyle("-fx-text-fill: gray;");
                                }
                            });
                        } catch (Exception e) {
                            Platform.runLater(() -> {
                                rsvpLabel.setText("RSVP: Unknown");
                                rsvpLabel.setStyle("-fx-text-fill: gray;");
                            });
                        }
                    });

                    rsvpThread.setDaemon(true);
                    rsvpThread.start();

                    // Add labels to container
                    container.getChildren().addAll(titleLabel, timeLabel);
                    if (groupLabel != null) {
                        container.getChildren().add(groupLabel);
                    }
                    container.getChildren().add(rsvpLabel);

                    setGraphic(container);
                }
            }
        };
    }

    private String formatEventTime(Event event) {
        LocalDateTime start = event.getStartTime();
        LocalDateTime end = event.getEndTime();

        if (end != null) {
            return formatter.format(start) + " to " + formatter.format(end);
        } else {
            return formatter.format(start);
        }
    }

    private void showEventDetails(Event event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Event Details");
        dialog.setHeaderText(event.getTitle());

        ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(closeButton);

        // Create the content
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        // Event details
        Label timeLabel = new Label("Time: " + formatEventTime(event));
        Label groupLabel = new Label("Group: " + (event.getGroup() != null ? event.getGroup().getName() : "None"));
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea(event.getDescription());
        descriptionArea.setEditable(false);
        descriptionArea.setPrefHeight(100);

        // RSVP section
        Label rsvpLabel = new Label("Your RSVP:");
        HBox rsvpButtons = new HBox(10);

        Button attendingButton = new Button("Attending");
        Button maybeButton = new Button("Maybe");
        Button declineButton = new Button("Decline");

        // Style buttons
        attendingButton.setStyle("-fx-base: lightgreen;");
        maybeButton.setStyle("-fx-base: lightyellow;");
        declineButton.setStyle("-fx-base: #FFCCCC;");

        rsvpButtons.getChildren().addAll(attendingButton, maybeButton, declineButton);
        rsvpButtons.setAlignment(Pos.CENTER);

        // Set the current selection
        try {
            Optional<EventAttendee> response = attendeeController.getUserResponse(event, currentUser);
            if (response.isPresent()) {
                EventAttendee.AttendanceStatus status = response.get().getStatus();
                switch (status) {
                    case ATTENDING:
                        attendingButton.setDisable(true);
                        break;
                    case MAYBE:
                        maybeButton.setDisable(true);
                        break;
                    case DECLINED:
                        declineButton.setDisable(true);
                        break;
                }
            }
        } catch (Exception e) {
            // Ignore
        }

        // RSVP button actions
        attendingButton.setOnAction(e -> {
            respondToEvent(event, EventAttendee.AttendanceStatus.ATTENDING);
            attendingButton.setDisable(true);
            maybeButton.setDisable(false);
            declineButton.setDisable(false);
        });

        maybeButton.setOnAction(e -> {
            respondToEvent(event, EventAttendee.AttendanceStatus.MAYBE);
            attendingButton.setDisable(false);
            maybeButton.setDisable(true);
            declineButton.setDisable(false);
        });

        declineButton.setOnAction(e -> {
            respondToEvent(event, EventAttendee.AttendanceStatus.DECLINED);
            attendingButton.setDisable(false);
            maybeButton.setDisable(false);
            declineButton.setDisable(true);
        });

        // Attendee list
        Label attendeesLabel = new Label("Attendees:");
        ListView<User> attendeesList = new ListView<>();
        attendeesList.setPrefHeight(150);

        // Load attendees
        Thread attendeesThread = new Thread(() -> {
            try {
                List<User> attendees = attendeeController.getAttendingUsers(event);
                Platform.runLater(() -> {
                    attendeesList.getItems().addAll(attendees);

                    // Set cell factory to display user names
                    attendeesList.setCellFactory(param -> new ListCell<>() {
                        @Override
                        protected void updateItem(User user, boolean empty) {
                            super.updateItem(user, empty);
                            if (empty || user == null) {
                                setText(null);
                            } else {
                                setText(user.getUsername());
                            }
                        }
                    });

                    // Show placeholder if no attendees
                    if (attendees.isEmpty()) {
                        Label placeholder = new Label("No attendees yet");
                        placeholder.setStyle("-fx-text-fill: gray;");
                        attendeesList.setPlaceholder(placeholder);
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Label placeholder = new Label("Could not load attendees");
                    placeholder.setStyle("-fx-text-fill: red;");
                    attendeesList.setPlaceholder(placeholder);
                });
            }
        });

        attendeesThread.setDaemon(true);
        attendeesThread.start();

        // Add all components to content
        content.getChildren().addAll(
                timeLabel,
                groupLabel,
                descriptionLabel,
                descriptionArea,
                new Separator(),
                rsvpLabel,
                rsvpButtons,
                new Separator(),
                attendeesLabel,
                attendeesList
        );

        dialog.getDialogPane().setContent(content);

        // Show the dialog
        dialog.showAndWait();

        // Refresh the event list when dialog is closed
        loadEvents();
    }

    private void respondToEvent(Event event, EventAttendee.AttendanceStatus status) {
        try {
            attendeeController.respondToEvent(event, currentUser, status);
            showAlert("Success", "Your RSVP has been updated");
        } catch (Exception e) {
            showAlert("Error", "Failed to update RSVP: " + e.getMessage());
        }
    }

    private void showAddEventDialog() {
        // Dialog setup
        Dialog<Event> dialog = new Dialog<>();
        dialog.setTitle("Add New Event");
        dialog.setHeaderText("Create a new event");

        // Set the button types
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        // Create fields
        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Event Description");

        // Date/time field
        TextField dateTimeField = new TextField();
        dateTimeField.setPromptText("yyyy-MM-dd HH:mm");
        dateTimeField.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));

        // Add group selection dropdown
        ComboBox<Group> groupComboBox = new ComboBox<>();
        groupComboBox.setPromptText("Select a group");

        // Load user's groups
        try {
            List<Group> userGroups = groupController.getUserGroups(currentUser);
            groupComboBox.getItems().addAll(userGroups);

            groupComboBox.setCellFactory(param -> new ListCell<>() {
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

            // Show group name in the combo box when selected
            groupComboBox.setButtonCell(new ListCell<>() {
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

            if (!userGroups.isEmpty()) {
                groupComboBox.setValue(userGroups.getFirst());
            }
        } catch (Exception e) {
            showAlert("Error", "Failed to load groups: " + e.getMessage());
        }

        VBox content = new VBox(10);
        content.setPadding(new Insets(20, 20, 10, 10));
        content.getChildren().addAll(
                new Label("Title:"), titleField,
                new Label("Description:"), descriptionField,
                new Label("Date and Time:"), dateTimeField,
                new Label("Group:"), groupComboBox
        );

        dialog.getDialogPane().setContent(content);

        // Set focus to the title field
        titleField.requestFocus();

        // Process the result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == createButtonType) {
                try {
                    final String title = titleField.getText();
                    if (title == null || title.trim().isEmpty()) {
                        showAlert("Error", "Title cannot be empty");
                        return null;
                    }

                    final String description = descriptionField.getText();

                    // Parse date/time
                    final LocalDateTime dateTime;
                    try {
                        dateTime = LocalDateTime.parse(
                                dateTimeField.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    } catch (DateTimeParseException e) {
                        showAlert("Error", "Invalid date format. Please use yyyy-MM-dd HH:mm");
                        return null;
                    }

                    // Get selected group
                    final Group selectedGroup = groupComboBox.getValue();
                    if (selectedGroup == null) {
                        showAlert("Error", "Please select a group");
                        return null;
                    }

                    // Use HibernateSessionManager to manage the session and transaction
                    Event newEvent = HibernateSessionManager.executeWithTransaction(session -> {
                        // Create and save the event
                        Event event = new Event(title, description, dateTime, dateTime.plusHours(1), selectedGroup);
                        session.save(event);
                        return event;
                    });

                    // Automatically RSVP the creator as attending
                    attendeeController.respondToEvent(newEvent, currentUser, EventAttendee.AttendanceStatus.ATTENDING);

                    return newEvent;
                } catch (Exception e) {
                    showAlert("Error", "Failed to create event: " + e.getMessage());
                    return null;
                }
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(event -> {
            if (event != null) {
                loadEvents(); // Refresh the list
                showAlert("Success", "Event created successfully!");
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Node getRoot() {
        return root;
    }
}