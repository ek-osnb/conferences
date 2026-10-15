package ek.osnb.demo.conferences;

import com.jayway.jsonpath.JsonPath;
import ek.osnb.demo.conferences.registration.RegistrationCreatedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@RecordApplicationEvents
class ConferenceFlowTest {

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private ApplicationEvents events;

    @Test
    void conferencesAreListedFirstOneFirst() {
        assertThat(mvc.get().uri("/api/conferences").exchange())
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asArray()
                .containsExactly("Øredev 2026", "Jfokus 2027", "JavaLand 2027",
                        "KubeCon + CloudNativeCon Europe 2027", "Devoxx France 2027");
    }

    @Test
    void conferencesCanBeSearchedByNameOrCity() {
        assertThat(mvc.get().uri("/api/conferences?q=sweden").exchange())
                .hasStatusOk()
                .bodyJson().extractingPath("$[*].name").asArray().containsExactly("Øredev 2026", "Jfokus 2027");

        assertThat(mvc.get().uri("/api/conferences?q=DEVOXX").exchange())
                .hasStatusOk()
                .bodyJson().extractingPath("$[*].name").asArray().containsExactly("Devoxx France 2027");
    }

    @Test
    void unknownConferenceReturns404AsProblemDetail() {
        MvcTestResult result = mvc.get().uri("/api/conferences/999999").exchange();

        assertThat(result).hasStatus(HttpStatus.NOT_FOUND);
        assertThat(result).hasContentType(MediaType.APPLICATION_PROBLEM_JSON);
        assertThat(result).bodyJson().extractingPath("$.detail").isEqualTo("Conference not found: 999999");
    }

    @Test
    void theProgrammeShowsTalksInOrder() {
        long conferenceId = findConferenceId("Jfokus 2027");

        MvcTestResult result = mvc.get().uri("/api/conferences/{id}/talks", conferenceId).exchange();

        assertThat(result).hasStatusOk();
        assertThat(result).bodyJson().extractingPath("$[*].title").asArray().containsExactly(
                "Spring Boot from zero to production",
                "Tests you actually want to run");
        assertThat(result).bodyJson().extractingPath("$[0].speaker").isEqualTo("Anna Holm");
        assertThat(result).bodyJson().extractingPath("$[0].level").isEqualTo("BEGINNER");
    }

    @Test
    void talksOfAnUnknownConferenceReturn404() {
        assertThat(mvc.get().uri("/api/conferences/999999/talks").exchange())
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    void registeringTakesASeat() {
        long conferenceId = findConferenceId("Jfokus 2027");
        long seatsBefore = seatsLeft(conferenceId);
        String email = uniqueEmail();

        MvcTestResult registered = register(conferenceId, "Anna Jensen", email);
        assertThat(registered).hasStatus(HttpStatus.CREATED);
        assertThat(registered).bodyJson().extractingPath("$.conferenceName").isEqualTo("Jfokus 2027");
        long registrationId = number(registered, "$.id");

        assertThat(events.stream(RegistrationCreatedEvent.class))
                .singleElement()
                .satisfies(event -> assertThat(event.registrationId()).isEqualTo(registrationId));

        assertThat(seatsLeft(conferenceId)).isEqualTo(seatsBefore - 1);

        assertThat(register(conferenceId, "Anna Jensen", email.toUpperCase()))
                .hasStatus(HttpStatus.CONFLICT)
                .bodyJson().extractingPath("$.detail").isEqualTo("You are already registered for Jfokus 2027");
        assertThat(seatsLeft(conferenceId)).isEqualTo(seatsBefore - 1);
    }

    @Test
    void aFullConferenceReturns409() {
        long conferenceId = findConferenceId("Øredev 2026");

        long seatsLeft = seatsLeft(conferenceId);
        for (int i = 0; i < seatsLeft; i++) {
            assertThat(register(conferenceId, "Guest " + i, uniqueEmail())).hasStatus(HttpStatus.CREATED);
        }

        assertThat(seatsLeft(conferenceId)).isZero();
        assertThat(register(conferenceId, "One too many", uniqueEmail()))
                .hasStatus(HttpStatus.CONFLICT)
                .bodyJson().extractingPath("$.detail").isEqualTo("Øredev 2026 is fully booked");
    }

    @Test
    void anInvalidRegistrationReturns400WithAnErrorPerField() {
        long conferenceId = findConferenceId("Devoxx France 2027");

        MvcTestResult result = register(conferenceId, "", "not-an-email");

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST);
        assertThat(result).bodyJson().extractingPath("$.errors.name").isNotNull();
        assertThat(result).bodyJson().extractingPath("$.errors.email").isNotNull();
    }

    @Test
    void registeringForAnUnknownConferenceReturns404() {
        assertThat(register(999999, "Anna Jensen", uniqueEmail()))
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    private long findConferenceId(String name) {
        MvcTestResult result = mvc.get().uri("/api/conferences?q={q}", name).exchange();
        List<Integer> ids = JsonPath.read(body(result), "$[?(@.name == '" + name + "')].id");

        assertThat(ids).as("conference %s", name).hasSize(1);

        return ids.getFirst();
    }

    private long seatsLeft(long conferenceId) {
        return number(mvc.get().uri("/api/conferences/{id}/availability", conferenceId).exchange(), "$.seatsLeft");
    }

    private MvcTestResult register(long conferenceId, String name, String email) {
        return mvc.post().uri("/api/conferences/{id}/registrations", conferenceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "%s", "email": "%s"}
                        """.formatted(name, email))
                .exchange();
    }

    private static String uniqueEmail() {
        return "test-" + UUID.randomUUID() + "@example.com";
    }

    private static String body(MvcTestResult result) {
        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }

    private static long number(MvcTestResult result, String path) {
        Number value = JsonPath.read(body(result), path);
        return value.longValue();
    }
}
