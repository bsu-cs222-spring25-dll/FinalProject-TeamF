package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.UserService;
import java.util.Optional;
import java.util.UUID;

public class UserController {
    private UserService userService;
    private User currentUser;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Add findById method to match the usage in InterestSelectionView
    public Optional<User> findById(UUID id) {
        return userService.findById(id);
    }

    //login the user that is already present
    public Optional<User> login(String username, String password) {
        Optional<User> loggedInUser = userService.login(username, password);
        loggedInUser.ifPresent(user -> this.currentUser = user);
        return loggedInUser;
    }

    //register if the user is not present
    public User register(String username, String email, String password) {
        return userService.registerUser(username, email, password);
    }

    //update user email
    public User updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        return userService.updateProfile(user);
    }

    //update user password
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userService.updateProfile(user);
    }

    //add interest
    public boolean addInterest(User user, Interest interest) {
        return userService.addInterest(user, interest);
    }

    //remove interest
    public boolean removeInterest(User user, Interest interest) {
        return userService.removeInterest(user, interest);
    }

    //get current user
    public User getCurrentUser() {
        return currentUser;
    }

    //set current user
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //log out the current user
    public void logout() {
        this.currentUser = null;
    }
}