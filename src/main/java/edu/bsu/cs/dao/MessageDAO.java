package edu.bsu.cs.dao;

import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.Message;
import edu.bsu.cs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MessageDAO extends GenericDAO<Message, UUID> {


    List<Message> findByGroup(Group group);

    List<Message> findRecentByGroup(Group group, int limit);


    List<Message> findBySender(User sender);

    List<Message> findAfterTime(LocalDateTime time);
}