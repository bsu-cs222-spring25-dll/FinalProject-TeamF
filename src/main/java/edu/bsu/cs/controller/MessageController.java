package edu.bsu.cs.controller;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.MessageService;

import java.util.List;

public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public void sendMessage(User sender, Group group, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        messageService.sendMessage(sender, group, content);
    }

    public List<Message> getGroupMessages(Group group) {
        return messageService.getGroupMessages(group);
    }
}

