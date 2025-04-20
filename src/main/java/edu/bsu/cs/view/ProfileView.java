package edu.bsu.cs.view;

import edu.bsu.cs.controller.UserController;
import edu.bsu.cs.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class ProfileView {
    private final UserController userController;
    private final User currentUser;
    private final VBox root;

    public ProfileView(UserController userController, User currentUser) {
        this.userController = userController;
        this.currentUser = currentUser;
        this.root = createProfileView();
    }

    public VBox getRoot() {
        return root;
    }

    private VBox createProfileView() {
        VBox profileContainer = new VBox(20);
        profileContainer.setPadding(new Insets(30));
        profileContainer.setAlignment(Pos.TOP_CENTER);

        // Profile header
        Text profileHeader = new Text("User Profile");
        profileHeader.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));

        // User information display
        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setHgap(10);
        userInfoGrid.setVgap(15);
        userInfoGrid.setPadding(new Insets(20));
        userInfoGrid.setAlignment(Pos.CENTER);

        // Username (read-only)
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField(currentUser.getUsername());
        usernameField.setEditable(false);
        usernameField.setPrefWidth(300);

        // Current Email
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField(currentUser.getEmail());
        emailField.setPrefWidth(300);

        // Update Email Button
        Button updateEmailButton = new Button("Update Email");
        updateEmailButton.setOnAction(e -> handleEmailUpdate(emailField.getText()));

        // Password Update Section
        Label currentPasswordLabel = new Label("Current Password:");
        PasswordField currentPasswordField = new PasswordField();
        currentPasswordField.setPromptText("Enter current password");
        currentPasswordField.setPrefWidth(300);

        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter new password");
        newPasswordField.setPrefWidth(300);

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm new password");
        confirmPasswordField.setPrefWidth(300);

        // Update Password Button
        Button updatePasswordButton = new Button("Update Password");
        updatePasswordButton.setOnAction(e -> handlePasswordUpdate(
                currentPasswordField.getText(),
                newPasswordField.getText(),
                confirmPasswordField.getText()));

        // Add components to grid
        int row = 0;
        userInfoGrid.add(usernameLabel, 0, row);
        userInfoGrid.add(usernameField, 1, row);

        row++;
        userInfoGrid.add(emailLabel, 0, row);
        userInfoGrid.add(emailField, 1, row);

        row++;
        userInfoGrid.add(new Label(""), 0, row);
        userInfoGrid.add(updateEmailButton, 1, row);

        row++;
        userInfoGrid.add(new Separator(), 0, row, 2, 1);

        row++;
        userInfoGrid.add(currentPasswordLabel, 0, row);
        userInfoGrid.add(currentPasswordField, 1, row);

        row++;
        userInfoGrid.add(newPasswordLabel, 0, row);
        userInfoGrid.add(newPasswordField, 1, row);

        row++;
        userInfoGrid.add(confirmPasswordLabel, 0, row);
        userInfoGrid.add(confirmPasswordField, 1, row);

        row++;
        userInfoGrid.add(new Label(""), 0, row);
        userInfoGrid.add(updatePasswordButton, 1, row);

        // Add everything to the main container
        profileContainer.getChildren().addAll(profileHeader, userInfoGrid);

        return profileContainer;
    }

    private void handleEmailUpdate(String newEmail) {
        if (newEmail == null || newEmail.trim().isEmpty()) {
            showAlert("Error", "Email cannot be empty");
            return;
        }

        try {
            User updatedUser = userController.updateEmail(currentUser, newEmail);
            showAlert("Success", "Email updated successfully");
        } catch (Exception e) {
            showAlert("Error", "Failed to update email: " + e.getMessage());
        }
    }

    private void handlePasswordUpdate(String currentPassword, String newPassword, String confirmPassword) {
        if (currentPassword == null || currentPassword.trim().isEmpty()) {
            showAlert("Error", "Current password cannot be empty");
            return;
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            showAlert("Error", "New password cannot be empty");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "New passwords don't match");
            return;
        }

        // Verify current password
        if (!currentUser.getPassword().equals(currentPassword)) {
            showAlert("Error", "Current password is incorrect");
            return;
        }

        try {
            User updatedUser = userController.updatePassword(currentUser, newPassword);
            showAlert("Success", "Password updated successfully");
        } catch (Exception e) {
            showAlert("Error", "Failed to update password: " + e.getMessage());
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