package org.university.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.dao.DataIntegrityViolationException;
import org.university.dto.CourseRequest;
import org.university.model.Course;
import org.university.repo.CourseRepository;
import org.university.service.EnrollmentService;

@Controller
public class CourseController {

    private final CourseRepository courseRepo;
    private final EnrollmentService enrollmentService;

    public CourseController(CourseRepository courseRepo,
                            EnrollmentService enrollmentService) {
        this.courseRepo = courseRepo;
        this.enrollmentService = enrollmentService;
    }

    // ===============================
    // View all courses
    // ===============================
    @GetMapping("/courses")
    public String courses(Model model, Authentication auth,
                          @RequestParam(required = false) String search) {

        // Resolve course list — filtered or full
        boolean isSearchActive = search != null && !search.isBlank();
        List<Course> courses = isSearchActive
                ? courseRepo.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(search, search)
                : courseRepo.findAll();

        model.addAttribute("courses", courses);
        model.addAttribute("search", isSearchActive ? search : "");

        // Check if user is ADMIN
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            model.addAttribute("courseRequest", new CourseRequest());
            List<Long> courseIds = courses.stream().map(Course::getId).toList();
            model.addAttribute("enrollmentCounts", enrollmentService.enrollmentCountsForCourses(courseIds));
        }

        // Load enrolled courses ONLY for students
        if (!isAdmin && auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            List<Long> enrolledCourseIds = enrollmentService
                    .myEnrollments(username)
                    .stream()
                    .map(e -> e.getCourse().getId())
                    .toList();
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);
        }

        return "courses";
    }

    // ===============================
    // ADMIN: Add new course
    // ===============================
    @PostMapping("/courses/new")
    public String addCourse(@Valid @ModelAttribute("courseRequest") CourseRequest request,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttrs) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseRepo.findAll());
            model.addAttribute("isAdmin", true);
            return "courses";
        }

        try {
            courseRepo.save(new Course(request.getCode(), request.getName(), request.getProfessor()));
            redirectAttrs.addFlashAttribute("successMessage",
                    "Course \"" + request.getCode() + "\" added successfully.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to add course: " + e.getMessage());
        }
        return "redirect:/courses";
    }

    // ===============================
    // ADMIN: Delete course
    // ===============================
    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        String code = courseRepo.findById(id).map(Course::getCode).orElse("#" + id);
        try {
            courseRepo.deleteById(id);
            redirectAttrs.addFlashAttribute("successMessage",
                    "Course \"" + code + "\" deleted successfully.");
        } catch (DataIntegrityViolationException e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Cannot delete this course because students are currently enrolled in it. " +
                    "Please drop all enrollments first.");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to delete course: " + e.getMessage());
        }
        return "redirect:/courses";
    }

    // ===============================
    // ADMIN: Drop all enrollments for a course
    // ===============================
    @PostMapping("/courses/{id}/drop-all-enrollments")
    public String dropAllEnrollments(@PathVariable Long id, RedirectAttributes redirectAttrs) {
        String code = courseRepo.findById(id).map(Course::getCode).orElse("#" + id);
        try {
            enrollmentService.dropAllForCourse(id);
            redirectAttrs.addFlashAttribute("successMessage",
                    "All enrollments dropped for course \"" + code + "\".");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("errorMessage",
                    "Failed to drop enrollments: " + e.getMessage());
        }
        return "redirect:/courses";
    }
}
