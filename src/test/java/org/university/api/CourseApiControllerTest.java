package org.university.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.university.model.Course;
import org.university.repo.CourseRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.profiles.active=dev",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class CourseApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseRepository courseRepository;

    @Test
    void get_courses_returns_200_when_authenticated() throws Exception {
        when(courseRepository.findAll()).thenReturn(
                List.of(new Course("CS612", "Web Services", "Prof. X"))
        );

        mockMvc.perform(get("/api/v1/courses")
                        .session(authenticatedSession("student1", "STUDENT")))
                .andExpect(status().isOk());
    }

    @Test
    void get_courses_returns_401_when_not_authenticated() throws Exception {
        mockMvc.perform(get("/api/v1/courses")
                        .header("X-Requested-With", "XMLHttpRequest"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void get_course_by_id_returns_404_for_non_existent_course() throws Exception {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/courses/999")
                        .session(authenticatedSession("student1", "STUDENT")))
                .andExpect(status().isNotFound());
    }

    @Test
    void post_course_returns_403_when_student_tries_to_create() throws Exception {
        String payload = """
                {
                  \"code\": \"CS999\",
                  \"name\": \"Distributed Systems\",
                  \"professor\": \"Prof. Z\"
                }
                """;

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(APPLICATION_JSON)
                        .content(payload)
                        .session(authenticatedSession("student1", "STUDENT")))
                .andExpect(status().isForbidden());

        verify(courseRepository, never()).save(any(Course.class));
    }

    private MockHttpSession authenticatedSession(String username, String role) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                "N/A",
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
        return session;
    }
}

