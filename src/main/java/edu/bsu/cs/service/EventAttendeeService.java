package edu.bsu.cs.service;

import edu.bsu.cs.dao.EventAttendeeDAO;
import edu.bsu.cs.model.Event;
import edu.bsu.cs.model.EventAttendee;
import edu.bsu.cs.model.User;
import java.util.List;
import java.util.Optional;


public class EventAttendeeService {
    private final EventAttendeeDAO eventAttendeeDAO;

    public EventAttendeeService(EventAttendeeDAO eventAttendeeDAO) {
        this.eventAttendeeDAO = eventAttendeeDAO;
    }

    public EventAttendee respondToEvent(Event event, User user, EventAttendee.AttendanceStatus status) {
        Optional<EventAttendee> existingAttendee = eventAttendeeDAO.findByEventAndUser(event, user);

        if (existingAttendee.isPresent()) {
            EventAttendee attendee = existingAttendee.get();
            attendee.setStatus(status);
            return eventAttendeeDAO.update(attendee);
        } else {
            EventAttendee attendee = new EventAttendee(event, user, status);
            return eventAttendeeDAO.save(attendee);
        }
    }

    public Optional<EventAttendee> getUserResponse(Event event, User user) {
        return eventAttendeeDAO.findByEventAndUser(event, user);
    }

    public List<User> getAttendingUsers(Event event) {
        return eventAttendeeDAO.findAttendingUsersByEvent(event);
    }

}