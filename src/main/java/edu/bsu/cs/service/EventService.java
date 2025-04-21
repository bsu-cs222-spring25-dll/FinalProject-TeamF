package edu.bsu.cs.service;

import edu.bsu.cs.dao.EventDAO;
import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import edu.bsu.cs.util.HibernateSessionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventService {
    private final EventDAO eventDAO;

    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public Event createEvent(String title, String description, LocalDateTime startTime,
                             LocalDateTime endTime, Group group) {
        return HibernateSessionManager.executeWithTransaction(session -> {
            Event event = new Event(title, description, startTime, endTime, group);
            session.save(event);
            return event;
        });
    }

    public Event updateEvent(Event event) {
        return eventDAO.update(event);
    }

    public boolean deleteEvent(UUID eventId) {
        return eventDAO.deleteById(eventId);
    }

    public Optional<Event> findById(UUID id) {
        return eventDAO.findById(id);
    }

    public List<Event> findUpcomingEvents(int limit) {
        return eventDAO.findUpcomingEvents(LocalDateTime.now(), limit);
    }

    public List<Event> findUpcomingEventsForUser(User user, int limit) {
        return eventDAO.findUpcomingEventsForUser(user, LocalDateTime.now(), limit);
    }

    public List<Event> findByGroup(Group group) {
        return eventDAO.findByGroup(group);
    }
}