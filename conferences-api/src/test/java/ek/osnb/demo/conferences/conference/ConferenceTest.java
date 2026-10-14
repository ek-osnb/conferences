package ek.osnb.demo.conferences.conference;

import ek.osnb.demo.conferences.common.InvalidInputException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConferenceTest {

    private static final LocalDate MAY_1 = LocalDate.of(2027, 5, 1);
    private static final LocalDate MAY_2 = LocalDate.of(2027, 5, 2);

    @Test
    void createKeepsTheValuesAndTrimsTheName() {
        Conference conference = Conference.create("  Harbour Java Days ", "Copenhagen", "Harbour Hall",
                MAY_1, MAY_2, "Java", 300);

        assertEquals("Harbour Java Days", conference.getName());
        assertEquals(MAY_1, conference.getStartDate());
        assertEquals(MAY_2, conference.getEndDate());
        assertEquals(300, conference.getCapacity());
    }

    @Test
    void aOneDayConferenceStartsAndEndsOnTheSameDay() {
        Conference conference = Conference.create("Meetup", "Odense", null, MAY_1, MAY_1, null, 3);

        assertEquals(conference.getStartDate(), conference.getEndDate());
    }

    @Test
    void aConferenceCannotEndBeforeItStarts() {
        InvalidInputException error = assertThrows(InvalidInputException.class,
                () -> Conference.create("Backwards", "Aarhus", null, MAY_2, MAY_1, null, 10));

        assertEquals("A conference cannot end before it starts", error.getMessage());
    }

    @Test
    void aConferenceNeedsRoomForAtLeastOnePerson() {
        assertThrows(InvalidInputException.class,
                () -> Conference.create("Empty", "Aarhus", null, MAY_1, MAY_1, null, 0));
    }

    @Test
    void nameAndCityAreRequired() {
        assertThrows(InvalidInputException.class,
                () -> Conference.create(" ", "Aarhus", null, MAY_1, MAY_1, null, 10));
        assertThrows(InvalidInputException.class,
                () -> Conference.create("No city", null, null, MAY_1, MAY_1, null, 10));
    }
}
