package org.university.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default
            "Password must be at least 8 characters and include uppercase, lowercase, number, special character, and no spaces";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
