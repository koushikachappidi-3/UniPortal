package org.university.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String courses(Model model, Authentication auth) {

        // Load all courses
        model.addAttribute("courses", courseRepo.findAll());

        // Check if user is ADMIN
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        // Seed empty form object for admin add-course form
        if (isAdmin) {
            model.addAttribute("courseRequest", new CourseRequest());
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
                            Authentication auth) {

        if (result.hasErrors()) {
            model.addAttribute("courses", courseRepo.findAll());
            model.addAttribute("isAdmin", true);
            return "courses";
        }

        courseRepo.save(new Course(request.getCode(), request.getName(), request.getProfessor()));
        return "redirect:/courses";
    }

    // ===============================
    // ADMIN: Delete course
    // ===============================
    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {

        courseRepo.deleteById(id);
        return "redirect:/courses";
    }
}
