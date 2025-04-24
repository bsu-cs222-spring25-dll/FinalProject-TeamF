package edu.bsu.cs.controller;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.EventService;
import java.util.List;

public class EventManager {
    private final EventService eventService;

    public EventManager(EventService eventService) {
        this.eventService = eventService;
    }

    public List<Event> findUpcomingEventsForUser(User user, int limit) {
        return eventService.findUpcomingEventsForUser(user, limit);
    }

}