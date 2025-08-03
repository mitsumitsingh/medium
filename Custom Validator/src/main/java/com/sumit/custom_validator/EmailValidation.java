package com.sumit.custom_validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class EmailValidation implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Common disposable email domains
    private static final Set<String> DISPOSABLE_DOMAINS = new HashSet<>(Arrays.asList("tempmail.org", "mailinator.com", "throwaway.email"));

    private boolean allowDisposable;
    private Set<String> allowedDomains;
    private Set<String> blockedDomains;
    private boolean requireTLD;

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        this.allowDisposable = constraintAnnotation.allowDisposable();
        this.allowedDomains = new HashSet<>(Arrays.asList(constraintAnnotation.allowedDomains()));
        this.blockedDomains = new HashSet<>(Arrays.asList(constraintAnnotation.blockedDomains()));
        this.requireTLD = constraintAnnotation.requireTLD();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        email = email.trim().toLowerCase();

        // Basic format validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            addCustomMessage(context, "Email format is invalid");
            return false;
        }

        String domain = extractDomain(email);

        // Check TLD requirement
        if (requireTLD && !domain.contains(".")) {
            addCustomMessage(context, "Email must have a valid top-level domain");
            return false;
        }

        // Check blocked domains
        if (blockedDomains.contains(domain)) {
            addCustomMessage(context, "Email domain is not allowed");
            return false;
        }

        // Check allowed domains (if specified)
        if (!allowedDomains.isEmpty() && !allowedDomains.contains(domain)) {
            addCustomMessage(context, "Email domain is not in the allowed list");
            return false;
        }

        // Check disposable email domains
        if (!allowDisposable && DISPOSABLE_DOMAINS.contains(domain)) {
            addCustomMessage(context, "Disposable email addresses are not allowed");
            return false;
        }

        return true;
    }

    private String extractDomain(String email) {
        int atIndex = email.lastIndexOf('@');
        return atIndex > 0 ? email.substring(atIndex + 1) : "";
    }

    private void addCustomMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
