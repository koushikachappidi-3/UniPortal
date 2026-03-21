package org.university.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_log")
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;

    public NotificationLog() {
    }

    public NotificationLog(String username, String courseCode, String eventType, LocalDateTime occurredAt) {
        this.username = username;
        this.courseCode = courseCode;
        this.eventType = eventType;
        this.occurredAt = occurredAt;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }
}

