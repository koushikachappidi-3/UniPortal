package org.university.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.university.model.AppUser;
import org.university.model.Course;
import org.university.model.Enrollment;
import org.university.repo.AppUserRepository;
import org.university.repo.CourseRepository;
import org.university.repo.EnrollmentRepository;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepo;
    private final CourseRepository courseRepo;
    private final AppUserRepository userRepo;

    public EnrollmentService(EnrollmentRepository enrollmentRepo,
                             CourseRepository courseRepo,
                             AppUserRepository userRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.courseRepo = courseRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public void enroll(String username, Long courseId) {

        AppUser student = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (enrollmentRepo.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalStateException("Already enrolled in this course");
        }

        enrollmentRepo.save(new Enrollment(student, course));
    }

    @Transactional
    public void drop(String username, Long courseId) {

        AppUser student = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Enrollment enrollment = enrollmentRepo.findByStudentIdAndCourseId(student.getId(), courseId)
                .orElseThrow(() -> new IllegalStateException("Enrollment not found"));

        enrollmentRepo.delete(enrollment);
    }

    public List<Enrollment> myEnrollments(String username) {
        AppUser student = userRepo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return enrollmentRepo.findAllByStudentId(student.getId());
    }
}
