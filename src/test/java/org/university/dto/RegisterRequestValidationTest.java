package org.university.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void valid_request_passes_validation() {
        RegisterRequest request = buildRequest("student1", "Strong@123", "Strong@123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    void blank_username_fails_validation() {
        RegisterRequest request = buildRequest("   ", "Strong@123", "Strong@123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v ->
                "username".equals(v.getPropertyPath().toString())
                        && "Username is required".equals(v.getMessage())));
    }

    @Test
    void blank_password_fails_validation() {
        RegisterRequest request = buildRequest("student1", "", "Strong@123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v ->
                "password".equals(v.getPropertyPath().toString())
                        && "Password is required".equals(v.getMessage())));
    }

    @Test
    void weak_password_fails_StrongPassword() {
        RegisterRequest request = buildRequest("student1", "weakpass", "weakpass");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.stream().anyMatch(v ->
                "password".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Password must be at least 8 characters")));
    }

    @Test
    void strong_password_passes_StrongPassword() {
        RegisterRequest request = buildRequest("student1", "Aa1@abcd", "Aa1@abcd");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertFalse(violations.stream().anyMatch(v ->
                "password".equals(v.getPropertyPath().toString())
                        && v.getMessage().contains("Password must be at least 8 characters")));
    }

    private RegisterRequest buildRequest(String username, String password, String confirmPassword) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        return request;
    }
}

