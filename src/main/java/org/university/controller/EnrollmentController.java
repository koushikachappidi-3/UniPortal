package org.university.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.university.service.EnrollmentService;

@Controller
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    // Enroll in a course
    @PostMapping("/courses/{courseId}/enroll")
    public String enroll(@PathVariable Long courseId,
                         Authentication auth) {
        enrollmentService.enroll(auth.getName(), courseId);
        return "redirect:/courses";
    }

    // Drop a course
    @PostMapping("/courses/{courseId}/drop")
    public String drop(@PathVariable Long courseId,
                       Authentication auth) {
        enrollmentService.drop(auth.getName(), courseId);
        return "redirect:/courses";
    }

    // View my enrollments
    @GetMapping("/my-courses")
    public String myCourses(Authentication auth, Model model) {
        model.addAttribute("enrollments", enrollmentService.myEnrollments(auth.getName()));
        return "my-courses";
    }
}
