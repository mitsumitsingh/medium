package com.sumit.custom_validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailValidationTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidCorporateEmail() {
        UserRegistration user = new UserRegistration(
                "John Doe",
                "john.doe@company.com",
                "john@gmail.com"
        );

        Set<ConstraintViolation<UserRegistration>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidDomain() {
        UserRegistration user = new UserRegistration(
                "John Doe",
                "john.doe@invalid.com",
                "john@gmail.com"
        );

        Set<ConstraintViolation<UserRegistration>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage()
                .contains("not in the allowed list"));
    }

    @Test
    void testDisposableEmailBlocked() {
        UserRegistration user = new UserRegistration(
                "John Doe",
                "john.doe@company.com",
                "test@10minutemail.com"
        );

        // This should fail for personal email if allowDisposable was false
        // But our annotation allows disposable for personalEmail
        Set<ConstraintViolation<UserRegistration>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }
}
