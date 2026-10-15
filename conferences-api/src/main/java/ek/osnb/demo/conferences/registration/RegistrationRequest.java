package ek.osnb.demo.conferences.registration;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

record RegistrationRequest(

        @NotBlank
        @Size(max = Registration.NAME_MAX_LENGTH)
        String name,

        @NotBlank
        @Email
        @Size(max = Registration.EMAIL_MAX_LENGTH)
        String email
) {
}
