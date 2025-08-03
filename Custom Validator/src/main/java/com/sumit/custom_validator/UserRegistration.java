package com.sumit.custom_validator;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistration {

    @NotBlank(message = "Name is required")
    private String name;

    @ValidEmail(
            allowDisposable = false,
            allowedDomains = {"company.com", "partner.org"},
            message = "Please provide a valid corporate email address"
    )
    private String corporateEmail;

    @ValidEmail(allowDisposable = true)
    private String personalEmail;

}