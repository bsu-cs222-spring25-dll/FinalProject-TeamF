package edu.bsu.cs.service;
import edu.bsu.cs.dao.MessageDAO;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MessageService
{
    private final MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO){
        this.messageDAO=messageDAO;
    }

    //to send a message
    public Message sendMessage(User sender,Group group,String content){
        //validate that the user is the member of the group
        if(!group.getMembers().contains(sender)){
            throw new IllegalArgumentException("User must be a member of the group to send message");
        }
        Message message=new Message(sender,group,content);
        return messageDAO.save(message);
    }

    //to get all messages in a group
    public List<Message> getGroupMessages(Group group){
        return messageDAO.findByGroup(group);
    }

    //to only get recent messages
    public List<Message> getRecentGroupMessages(Group group,int limit){
        return messageDAO.findRecentByGroup(group,limit);
    }

    //gets all messages from a specific user
    public List<Message> getUserMessages(User user){
        return messageDAO.findBySender(user);
    }

    //retrieve messages on time
    public List<Message>getNewMessages(LocalDateTime since){
        return messageDAO.findAfterTime(since);
    }

    //find message by id
    public Optional<Message> findById(UUID id){
        return messageDAO.findById(id);
    }
}
