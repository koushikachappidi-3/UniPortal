package org.university.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.university.repo.AppUserRepository;
import org.university.repo.CourseRepository;
import org.university.repo.EnrollmentRepository;

@Controller
public class DashboardController {

    private final AppUserRepository appUserRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;

    public DashboardController(AppUserRepository appUserRepository,
                               CourseRepository courseRepository,
                               EnrollmentRepository enrollmentRepository) {
        this.appUserRepository = appUserRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        model.addAttribute("currentPage", "dashboard");
        model.addAttribute("username", username);
        model.addAttribute("role", isAdmin ? "ADMIN" : "STUDENT");
        model.addAttribute("isAdmin", isAdmin);

        if (isAdmin) {
            model.addAttribute("totalStudents", appUserRepository.countByRole("STUDENT"));
            model.addAttribute("totalCourses", courseRepository.count());
            model.addAttribute("totalEnrollments", enrollmentRepository.count());
        }

        return "dashboard";
    }
}

