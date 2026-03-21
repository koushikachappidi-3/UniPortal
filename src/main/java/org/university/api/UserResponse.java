package org.university.api;

import io.swagger.v3.oas.annotations.media.Schema;
import org.university.model.AppUser;

@Schema(description = "Current user response payload")
public class UserResponse {

    private Long id;
    private String username;
    private String role;

    public UserResponse() {
    }

    public UserResponse(Long id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public static UserResponse fromEntity(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}

