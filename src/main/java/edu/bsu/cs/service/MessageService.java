package edu.bsu.cs.service;
import edu.bsu.cs.dao.MessageDAO;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;
import java.util.List;

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

    //gets all messages from a specific user
    public List<Message> getUserMessages(User user){
        return messageDAO.findBySender(user);
    }

}
