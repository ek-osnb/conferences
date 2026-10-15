package ek.osnb.demo.conferences.registration;

import java.time.Instant;

public record RegistrationCreatedEvent(
        Long registrationId,
        Long conferenceId,
        String email,
        Instant registeredAt
) {
}
