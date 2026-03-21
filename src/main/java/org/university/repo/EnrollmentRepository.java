package org.university.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.university.model.Enrollment;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);

    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    List<Enrollment> findAllByStudentId(Long studentId);

    long countByCourseId(Long courseId);

    void deleteAllByCourseId(Long courseId);
}

