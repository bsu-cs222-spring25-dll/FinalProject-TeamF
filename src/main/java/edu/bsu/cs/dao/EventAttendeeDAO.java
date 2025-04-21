package edu.bsu.cs.dao;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EventAttendeeDAO extends GenericDAO<EventAttendee, UUID> {
    List<EventAttendee> findByEvent(Event event);
    List<EventAttendee> findByUser(User user);
    Optional<EventAttendee> findByEventAndUser(Event event, User user);
    List<User> findAttendingUsersByEvent(Event event);
}