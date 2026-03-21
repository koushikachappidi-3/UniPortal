package org.university.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.university.model.Enrollment;
import org.university.service.EnrollmentService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentApiController {

    private final EnrollmentService enrollmentService;

    public EnrollmentApiController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Enroll in a course", description = "Enrolls the current authenticated user in the given course")
    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestParam Long courseId, Authentication authentication) {
        String username = authentication.getName();

        try {
            enrollmentService.enroll(username, courseId);

            Enrollment created = enrollmentService.myEnrollments(username).stream()
                    .filter(e -> e.getCourse().getId().equals(courseId))
                    .findFirst()
                    .orElse(null);

            if (created == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("message", "Enrollment was created but could not be retrieved"));
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(EnrollmentResponse.fromEntity(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", ex.getMessage()));
        }
    }

    @Operation(summary = "Drop a course", description = "Drops enrollment for the current authenticated user by course id")
    @DeleteMapping("/drop/{courseId}")
    public ResponseEntity<?> drop(@PathVariable Long courseId, Authentication authentication) {
        String username = authentication.getName();

        Enrollment existing = enrollmentService.myEnrollments(username).stream()
                .filter(e -> e.getCourse().getId().equals(courseId))
                .findFirst()
                .orElse(null);

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Enrollment not found for course id: " + courseId));
        }

        try {
            enrollmentService.drop(username, courseId);
            return ResponseEntity.ok(EnrollmentResponse.fromEntity(existing));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", ex.getMessage()));
        }
    }

    @Operation(summary = "Get my enrollments", description = "Returns all enrollments for the current authenticated user")
    @GetMapping("/my-courses")
    public ResponseEntity<List<EnrollmentResponse>> myCourses(Authentication authentication) {
        String username = authentication.getName();

        List<EnrollmentResponse> response = enrollmentService.myEnrollments(username).stream()
                .map(EnrollmentResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }
}

