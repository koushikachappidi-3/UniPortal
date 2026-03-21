package org.university.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.university.model.Course;
import org.university.repo.CourseRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseApiController {

    private final CourseRepository courseRepository;

    public CourseApiController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Operation(summary = "Get all courses", description = "Returns all courses for any authenticated user")
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        List<CourseResponse> response = courseRepository.findAll()
                .stream()
                .map(CourseResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get course by id", description = "Returns a single course for any authenticated user")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCourseById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .<ResponseEntity<?>>map(course -> ResponseEntity.ok(CourseResponse.fromEntity(course)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Course not found with id: " + id)));
    }

    @Operation(summary = "Create course", description = "Creates a new course (ADMIN only)")
    @PostMapping
    public ResponseEntity<?> createCourse(@Valid @RequestBody CourseApiRequest request,
                                          Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only ADMIN users can create courses"));
        }

        Course saved = courseRepository.save(
                new Course(request.getCode(), request.getName(), request.getProfessor())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(CourseResponse.fromEntity(saved));
    }

    @Operation(summary = "Delete course", description = "Deletes a course by id (ADMIN only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id, Authentication authentication) {
        if (!isAdmin(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message", "Only ADMIN users can delete courses"));
        }

        if (!courseRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Course not found with id: " + id));
        }

        courseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}

