package edu.bsu.cs.controller;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.EventAttendeeService;

import java.util.List;
import java.util.Optional;

public class EventAttendeeController {
    private final EventAttendeeService eventAttendeeService;

    public EventAttendeeController(EventAttendeeService eventAttendeeService) {
        this.eventAttendeeService = eventAttendeeService;
    }

    public EventAttendee respondToEvent(Event event, User user, EventAttendee.AttendanceStatus status) {
        return eventAttendeeService.respondToEvent(event, user, status);
    }

    public Optional<EventAttendee> getUserResponse(Event event, User user) {
        return eventAttendeeService.getUserResponse(event, user);
    }

    public List<User> getAttendingUsers(Event event) {
        return eventAttendeeService.getAttendingUsers(event);
    }

}