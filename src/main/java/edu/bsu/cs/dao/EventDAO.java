package edu.bsu.cs.dao;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventDAO extends GenericDAO<Event, UUID> {
    List<Event> findByGroup(Group group);
    List<Event> findUpcomingEvents(LocalDateTime from, int limit);
    List<Event> findUpcomingEventsForUser(User user, LocalDateTime from, int limit);
}