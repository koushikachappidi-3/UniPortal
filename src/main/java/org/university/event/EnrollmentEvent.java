package org.university.event;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class EnrollmentEvent extends ApplicationEvent {

    private final String username;
    private final Long courseId;
    private final String courseCode;
    private final String eventType;
    private final LocalDateTime occurredAt;

    public EnrollmentEvent(Object source,
                           String username,
                           Long courseId,
                           String courseCode,
                           String eventType,
                           LocalDateTime occurredAt) {
        super(source);
        this.username = username;
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
    }

    public String getUsername() {
        return username;
    }

    public Long getCourseId() {
        return courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }
}

