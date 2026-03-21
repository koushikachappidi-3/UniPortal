package org.university.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.university.model.NotificationLog;
import org.university.repo.NotificationLogRepository;

@Component
public class NotificationLogListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationLogListener.class);

    private final NotificationLogRepository notificationLogRepository;

    public NotificationLogListener(NotificationLogRepository notificationLogRepository) {
        this.notificationLogRepository = notificationLogRepository;
    }

    @Async
    @EventListener
    public void handleEnrollmentEvent(EnrollmentEvent event) {
        NotificationLog entry = new NotificationLog(
                event.getUsername(),
                event.getCourseCode(),
                event.getEventType(),
                event.getOccurredAt()
        );

        notificationLogRepository.save(entry);
        log.info("Notification logged: {} {} {}", event.getUsername(), event.getEventType(), event.getCourseCode());
    }
}

