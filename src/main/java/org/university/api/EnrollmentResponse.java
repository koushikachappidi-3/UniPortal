package org.university.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.university.model.Enrollment;

import java.time.LocalDateTime;

@Schema(description = "Enrollment response payload")
public class EnrollmentResponse {

    private Long id;
    private String courseCode;
    private String courseName;
    private LocalDateTime enrolledAt;

    public EnrollmentResponse() {
    }

    public EnrollmentResponse(Long id, String courseCode, String courseName, LocalDateTime enrolledAt) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.enrolledAt = enrolledAt;
    }

    public static EnrollmentResponse fromEntity(Enrollment enrollment) {
        return new EnrollmentResponse(
                enrollment.getId(),
                enrollment.getCourse().getCode(),
                enrollment.getCourse().getName(),
                enrollment.getEnrolledAt()
        );
    }

    public Long getId() {
        return id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }
}

