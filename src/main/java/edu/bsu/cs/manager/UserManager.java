package edu.bsu.cs.manager;

import edu.bsu.cs.model.Interest;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserManager {

    private final UserService userService;
    private User currentUser;

    public UserManager(UserService userService) {
        this.userService = userService;
    }

    public Optional<User> findById(UUID id) {
        return userService.findById(id);
    }

    public void updateUserInterests(User user) {
        user.getInterests();
        userService.updateProfile(user);
    }


    public Optional<User> login(String username, String password) {
        Optional<User> loggedInUser = userService.login(username, password);
        loggedInUser.ifPresent(user -> this.currentUser = user);
        return loggedInUser;
    }

    public User register(String username, String email, String password) {
        return userService.registerUser(username, email, password);
    }

    public User updateEmail(User user, String newEmail) {
        user.setEmail(newEmail);
        return userService.updateProfile(user);
    }

    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userService.updateProfile(user);
    }

    public boolean addInterest(User user, Interest interest) {
        return userService.addInterest(user, interest);
    }

    public boolean removeInterest(User user, Interest interest) {
        return userService.removeInterest(user, interest);
    }
}