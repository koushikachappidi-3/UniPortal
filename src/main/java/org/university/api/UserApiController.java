package org.university.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.university.repo.AppUserRepository;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserApiController {

    private final AppUserRepository appUserRepository;

    public UserApiController(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Operation(summary = "Get current user", description = "Returns the currently authenticated user info without password hash")
    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        return appUserRepository.findByUsername(principal.getName())
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(UserResponse.fromEntity(user)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "User not found for username: " + principal.getName())));
    }
}

