package ek.osnb.demo.conferences.registration;

import ek.osnb.demo.conferences.common.ConflictException;
import ek.osnb.demo.conferences.common.NotFoundException;
import ek.osnb.demo.conferences.conference.ConferenceApi;
import ek.osnb.demo.conferences.conference.ConferenceInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private static final ConferenceInfo OREDEV = new ConferenceInfo(1L, "Øredev 2026", 3);

    @Mock
    private RegistrationRepository repository;

    @Mock
    private ConferenceApi conferences;

    @Mock
    private ApplicationEventPublisher events;

    @InjectMocks
    private RegistrationService service;

    @Test
    void registeringSavesTheRegistrationAndPublishesAnEvent() {
        when(conferences.getConference(OREDEV.id())).thenReturn(OREDEV);
        when(repository.countByConferenceId(OREDEV.id())).thenReturn(2L);
        when(repository.save(any(Registration.class))).thenAnswer(call -> call.getArgument(0));

        RegistrationResponse response = service.register(OREDEV.id(),
                new RegistrationRequest("Anna Holm", "Anna@Example.com"));

        assertEquals("Øredev 2026", response.conferenceName());
        assertEquals("anna@example.com", response.email());

        ArgumentCaptor<RegistrationCreatedEvent> event = ArgumentCaptor.forClass(RegistrationCreatedEvent.class);
        verify(events).publishEvent(event.capture());
        assertEquals(OREDEV.id(), event.getValue().conferenceId());
        assertEquals("anna@example.com", event.getValue().email());
    }

    @Test
    void theSameEmailCannotRegisterTwiceInAnyCase() {
        when(conferences.getConference(OREDEV.id())).thenReturn(OREDEV);
        when(repository.existsByConferenceIdAndEmail(OREDEV.id(), "anna@example.com")).thenReturn(true);

        ConflictException error = assertThrows(ConflictException.class,
                () -> service.register(OREDEV.id(), new RegistrationRequest("Anna", " ANNA@example.com")));

        assertEquals("You are already registered for Øredev 2026", error.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void aFullConferenceTakesNoMoreRegistrations() {
        when(conferences.getConference(OREDEV.id())).thenReturn(OREDEV);
        when(repository.countByConferenceId(OREDEV.id())).thenReturn(3L);

        ConflictException error = assertThrows(ConflictException.class,
                () -> service.register(OREDEV.id(), new RegistrationRequest("Anna", "anna@example.com")));

        assertEquals("Øredev 2026 is fully booked", error.getMessage());
        verify(repository, never()).save(any());
        verify(events, never()).publishEvent(any());
    }

    @Test
    void anUnknownConferenceIsNotFound() {
        when(conferences.getConference(99L)).thenThrow(new NotFoundException("Conference not found: 99"));

        assertThrows(NotFoundException.class,
                () -> service.register(99L, new RegistrationRequest("Anna", "anna@example.com")));
        verify(repository, never()).save(any());
    }

    @Test
    void seatsLeftIsNeverNegative() {
        when(conferences.getConference(OREDEV.id())).thenReturn(OREDEV);
        when(repository.countByConferenceId(OREDEV.id())).thenReturn(5L);

        AvailabilityResponse availability = service.availability(OREDEV.id());

        assertEquals(5, availability.registered());
        assertEquals(0, availability.seatsLeft());
    }
}
