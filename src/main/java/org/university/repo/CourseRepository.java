package org.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

