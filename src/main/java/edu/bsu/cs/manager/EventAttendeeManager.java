package edu.bsu.cs.manager;

import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.User;
import edu.bsu.cs.service.EventAttendeeService;

import java.util.List;
import java.util.Optional;

public class EventAttendeeManager {
    private final EventAttendeeService eventAttendeeService;

    public EventAttendeeManager(EventAttendeeService eventAttendeeService) {
        this.eventAttendeeService = eventAttendeeService;
    }

    public void respondToEvent(Event event, User user, EventAttendee.AttendanceStatus status) {
        eventAttendeeService.respondToEvent(event, user, status);
    }

    public Optional<EventAttendee> getUserResponse(Event event, User user) {
        return eventAttendeeService.getUserResponse(event, user);
    }

    public List<User> getAttendingUsers(Event event) {
        return eventAttendeeService.getAttendingUsers(event);
    }

}