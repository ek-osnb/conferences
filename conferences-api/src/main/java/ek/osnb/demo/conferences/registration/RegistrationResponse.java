package ek.osnb.demo.conferences.registration;

import java.time.Instant;

record RegistrationResponse(
        Long id,
        Long conferenceId,
        String conferenceName,
        String name,
        String email,
        Instant registeredAt
) {

    static RegistrationResponse from(Registration registration, String conferenceName) {
        return new RegistrationResponse(
                registration.getId(),
                registration.getConferenceId(),
                conferenceName,
                registration.getName(),
                registration.getEmail(),
                registration.getRegisteredAt()
        );
    }
}
