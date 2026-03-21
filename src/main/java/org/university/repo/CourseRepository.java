package org.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code, String name);
}

