package org.university.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;
import org.university.event.EnrollmentEvent;
import org.university.model.AppUser;
import org.university.model.Course;
import org.university.model.Enrollment;
import org.university.repo.AppUserRepository;
import org.university.repo.CourseRepository;
import org.university.repo.EnrollmentRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void enroll_success() {
        AppUser student = new AppUser("student1", "hash", "STUDENT");
        ReflectionTestUtils.setField(student, "id", 100L);

        Course course = new Course("CS612", "Web Services", "Prof. X");
        ReflectionTestUtils.setField(course, "id", 10L);

        when(appUserRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(100L, 10L)).thenReturn(false);

        enrollmentService.enroll("student1", 10L);

        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentRepository).save(enrollmentCaptor.capture());
        Enrollment saved = enrollmentCaptor.getValue();
        assertEquals(student, saved.getStudent());
        assertEquals(course, saved.getCourse());

        verify(applicationEventPublisher).publishEvent(any(EnrollmentEvent.class));
    }

    @Test
    void enroll_throwsWhenAlreadyEnrolled() {
        AppUser student = new AppUser("student1", "hash", "STUDENT");
        ReflectionTestUtils.setField(student, "id", 100L);

        Course course = new Course("CS612", "Web Services", "Prof. X");
        ReflectionTestUtils.setField(course, "id", 10L);

        when(appUserRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.existsByStudentIdAndCourseId(100L, 10L)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> enrollmentService.enroll("student1", 10L));

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @Test
    void enroll_throwsWhenCourseNotFound() {
        AppUser student = new AppUser("student1", "hash", "STUDENT");
        ReflectionTestUtils.setField(student, "id", 100L);

        when(appUserRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> enrollmentService.enroll("student1", 999L));

        verify(enrollmentRepository, never()).save(any(Enrollment.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }

    @Test
    void drop_success() {
        AppUser student = new AppUser("student1", "hash", "STUDENT");
        ReflectionTestUtils.setField(student, "id", 100L);

        Course course = new Course("CS612", "Web Services", "Prof. X");
        ReflectionTestUtils.setField(course, "id", 10L);

        Enrollment enrollment = new Enrollment(student, course);

        when(appUserRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudentIdAndCourseId(100L, 10L)).thenReturn(Optional.of(enrollment));

        enrollmentService.drop("student1", 10L);

        verify(enrollmentRepository).delete(enrollment);
        verify(applicationEventPublisher).publishEvent(any(EnrollmentEvent.class));
    }

    @Test
    void drop_throwsWhenEnrollmentNotFound() {
        AppUser student = new AppUser("student1", "hash", "STUDENT");
        ReflectionTestUtils.setField(student, "id", 100L);

        when(appUserRepository.findByUsername("student1")).thenReturn(Optional.of(student));
        when(enrollmentRepository.findByStudentIdAndCourseId(100L, 99L)).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> enrollmentService.drop("student1", 99L));

        verify(enrollmentRepository, never()).delete(any(Enrollment.class));
        verify(applicationEventPublisher, never()).publishEvent(any());
    }
}

