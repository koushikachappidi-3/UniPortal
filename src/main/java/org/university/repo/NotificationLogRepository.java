package org.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.model.NotificationLog;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
}

