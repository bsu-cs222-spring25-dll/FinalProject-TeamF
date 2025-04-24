package edu.bsu.cs.controller;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private final UserService userService;

    public UserManager(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findById(UUID id) {
        return userService.findById(id);
    }

    public void updateUserInterests(User user) {
        userService.updateProfile(user);
    }


    public Optional<User> login(String username, String password) {
        Optional<User> loggedInUser = userService.login(username, password);
        loggedInUser.ifPresent(user -> {
        });
        return loggedInUser;
    }

    public boolean addInterest(User user, Interest interest) {
        return userService.addInterest(user, interest);
    }

    public void removeInterest(User user, Interest interest) {
        userService.removeInterest(user, interest);
    }

}
