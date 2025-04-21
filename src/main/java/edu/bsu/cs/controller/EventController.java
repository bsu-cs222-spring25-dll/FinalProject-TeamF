package edu.bsu.cs.controller;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.Group;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public Event createEvent(String title, String description, LocalDateTime startTime,
                             LocalDateTime endTime, Group group) {
        return eventService.createEvent(title, description, startTime, endTime, group);
    }

    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    public boolean deleteEvent(UUID eventId) {
        return eventService.deleteEvent(eventId);
    }

    public Optional<Event> findById(UUID id) {
        return eventService.findById(id);
    }

    public List<Event> findUpcomingEvents(int limit) {
        return eventService.findUpcomingEvents(limit);
    }

    public List<Event> findUpcomingEventsForUser(User user, int limit) {
        return eventService.findUpcomingEventsForUser(user, limit);
    }

    public List<Event> findByGroup(Group group) {
        return eventService.findByGroup(group);
    }
}