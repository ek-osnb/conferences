package ek.osnb.demo.conferences.registration;

import ek.osnb.demo.conferences.common.ConflictException;
import ek.osnb.demo.conferences.conference.ConferenceApi;
import ek.osnb.demo.conferences.conference.ConferenceInfo;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
class RegistrationService {

    private final RegistrationRepository registrations;
    private final ConferenceApi conferences;
    private final ApplicationEventPublisher events;

    RegistrationService(
            RegistrationRepository registrations,
            ConferenceApi conferences,
            ApplicationEventPublisher events) {

        this.registrations = registrations;
        this.conferences = conferences;
        this.events = events;
    }

    RegistrationResponse register(Long conferenceId, RegistrationRequest request) {
        ConferenceInfo conference = conferences.getConference(conferenceId);
        String email = Registration.normalizeEmail(request.email());

        if (registrations.existsByConferenceIdAndEmail(conferenceId, email)) {
            throw new ConflictException("You are already registered for " + conference.name());
        }

        if (registrations.countByConferenceId(conferenceId) >= conference.capacity()) {
            throw new ConflictException(conference.name() + " is fully booked");
        }

        Registration registration = registrations.save(
                Registration.create(conferenceId, request.name(), email, Instant.now()));

        events.publishEvent(new RegistrationCreatedEvent(
                registration.getId(),
                conferenceId,
                registration.getEmail(),
                registration.getRegisteredAt()));

        return RegistrationResponse.from(registration, conference.name());
    }

    @Transactional(readOnly = true)
    AvailabilityResponse availability(Long conferenceId) {
        ConferenceInfo conference = conferences.getConference(conferenceId);
        long registered = registrations.countByConferenceId(conferenceId);

        long seatsLeft = Math.max(0, conference.capacity() - registered);

        return new AvailabilityResponse(conferenceId, conference.capacity(), registered, seatsLeft);
    }
}
