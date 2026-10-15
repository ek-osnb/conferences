package ek.osnb.demo.conferences.registration;

import ek.osnb.demo.conferences.common.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegistrationTest {

    @Test
    void emailIsStoredInLowerCaseWithoutSpaces() {
        Registration registration = Registration.create(1L, " Anna Holm ", " Anna@Example.COM ", Instant.now());

        assertEquals("anna@example.com", registration.getEmail());
        assertEquals("Anna Holm", registration.getName());
    }

    @Test
    void nameAndEmailAreRequired() {
        assertThrows(InvalidInputException.class,
                () -> Registration.create(1L, "", "anna@example.com", Instant.now()));
        assertThrows(InvalidInputException.class,
                () -> Registration.create(1L, "Anna", null, Instant.now()));
    }

    @Test
    void aRegistrationBelongsToAConference() {
        assertThrows(InvalidInputException.class,
                () -> Registration.create(null, "Anna", "anna@example.com", Instant.now()));
    }
}
