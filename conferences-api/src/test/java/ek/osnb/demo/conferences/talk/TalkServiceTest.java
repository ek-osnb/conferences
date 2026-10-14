package ek.osnb.demo.conferences.talk;

import ek.osnb.demo.conferences.common.NotFoundException;
import ek.osnb.demo.conferences.conference.ConferenceApi;
import ek.osnb.demo.conferences.conference.ConferenceInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TalkServiceTest {

    private static final ConferenceInfo JFOKUS = new ConferenceInfo(2L, "Jfokus 2027", 300);

    @Mock
    private TalkRepository repository;

    @Mock
    private ConferenceApi conferences;

    @InjectMocks
    private TalkService service;

    @Test
    void theProgrammeListsTheTalksOfTheConference() {
        when(conferences.getConference(JFOKUS.id())).thenReturn(JFOKUS);
        when(repository.findByConferenceIdOrderByStartsAt(JFOKUS.id())).thenReturn(List.of(
                talk("Spring Boot from zero", "Anna Holm"),
                talk("Tests you want to run", "Mikkel Bach")));

        List<TalkResponse> programme = service.findByConference(JFOKUS.id());

        assertEquals(List.of("Spring Boot from zero", "Tests you want to run"),
                programme.stream().map(TalkResponse::title).toList());
        assertEquals("Anna Holm", programme.getFirst().speaker());
    }

    @Test
    void anUnknownConferenceIsNotFoundAndNoTalksAreLoaded() {
        when(conferences.getConference(99L)).thenThrow(new NotFoundException("Conference not found: 99"));

        assertThrows(NotFoundException.class, () -> service.findByConference(99L));
        verifyNoInteractions(repository);
    }

    private static Talk talk(String title, String speaker) {
        return Talk.create(JFOKUS.id(), title, null, speaker,
                LocalDateTime.of(2027, 3, 10, 9, 30), "Hall A", Level.BEGINNER);
    }
}
