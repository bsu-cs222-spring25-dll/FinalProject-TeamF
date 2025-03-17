package edu.bsu.cs.controller;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.MessageService;
import java.util.List;

public class MessageController
{
    private final MessageService messageService;

    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    //send a message
    public Message sendMessage(User sender, Group group,String content){
        if(content==null || content.trim().isEmpty()){
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        return messageService.sendMessage(sender, group, content);
    }

    //get all messages of a particular group
    public List<Message> getGroupMessages(Group group){
        return messageService.getGroupMessages(group);
    }

}
