package edu.bsu.cs.service;

import edu.bsu.cs.dao.EventDAO;
import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventService {
    private final EventDAO eventDAO;

    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
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


    public List<Event> findUpcomingEventsForUser(User user, int limit) {
        return eventDAO.findUpcomingEventsForUser(user, LocalDateTime.now(), limit);
    }


}