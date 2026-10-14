package ek.osnb.demo.conferences.talk;

import ek.osnb.demo.conferences.conference.ConferenceApi;
import ek.osnb.demo.conferences.conference.ConferenceInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Order(2)
class TalkInitData implements CommandLineRunner {

    private final TalkRepository talks;
    private final ConferenceApi conferenceApi;

    private final Map<String, Long> conferenceIds = new HashMap<>();

    TalkInitData(TalkRepository talks, ConferenceApi conferenceApi) {
        this.talks = talks;
        this.conferenceApi = conferenceApi;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (talks.count() > 0) {
            return;
        }

        for (ConferenceInfo conference : conferenceApi.findAll()) {
            conferenceIds.put(conference.name(), conference.id());
        }

        talk("Øredev 2026", "Designing tables that last", "Freja Dahl",
                "2026-11-04T10:00", "Room 1", Level.BEGINNER,
                "Keys, relations and the questions to ask before you create a table.");
        talk("Øredev 2026", "Why is my query slow?", "Freja Dahl",
                "2026-11-05T13:00", "Room 1", Level.INTERMEDIATE,
                "Reading a query plan and adding the right index.");

        talk("Jfokus 2027", "Spring Boot from zero to production", "Anna Holm",
                "2027-02-09T09:30", "Room A", Level.BEGINNER,
                "Build a REST API with Spring Boot and JPA, test it and put it in a Docker image.");
        talk("Jfokus 2027", "Tests you actually want to run", "Mikkel Bach",
                "2027-02-09T11:00", "Room B", Level.INTERMEDIATE,
                "Fast unit tests, focused integration tests and how to tell which one you need.");

        talk("JavaLand 2027", "Package by feature", "Anna Holm",
                "2027-03-02T10:00", "Room 1", Level.INTERMEDIATE,
                "One package per feature, and why features should only know each other by id.");
        talk("JavaLand 2027", "Securing a REST API", "Omar Nasser",
                "2027-03-03T13:00", "Room 2", Level.ADVANCED,
                "Login, roles and the mistakes attackers look for first.");

        talk("KubeCon + CloudNativeCon Europe 2027", "Docker images for Java, done right", "Sara Lind",
                "2027-03-16T09:30", "Room 1", Level.INTERMEDIATE,
                "Multi-stage builds, small runtime images and not running as root.");
        talk("KubeCon + CloudNativeCon Europe 2027", "From git push to production", "Lucas Weber",
                "2027-03-16T13:00", "Room 2", Level.BEGINNER,
                "A tour of a CI/CD pipeline with GitHub Actions, from the first test to a released image.");
        talk("KubeCon + CloudNativeCon Europe 2027", "Kubernetes for the rest of us", "Sara Lind",
                "2027-03-17T10:00", "Room 1", Level.ADVANCED,
                "What Kubernetes gives you over Docker Compose, and when it is worth it.");

        talk("Devoxx France 2027", "You might not need a framework", "Jonas Berg",
                "2027-04-08T09:30", "Main stage", Level.BEGINNER,
                "A single-page app with plain JavaScript modules, no build step and no npm.");
        talk("Devoxx France 2027", "Forms everyone can use", "Ida Mortensen",
                "2027-04-08T11:00", "Main stage", Level.INTERMEDIATE,
                "Labels, error messages and keyboard navigation that work for all users.");
        talk("Devoxx France 2027", "Client-side routing explained", "Jonas Berg",
                "2027-04-09T10:00", "Room 2", Level.INTERMEDIATE,
                "How the History API turns one HTML page into many pages, and what the server must do.");
    }

    private void talk(
            String conferenceName,
            String title,
            String speaker,
            String startsAt,
            String room,
            Level level,
            String description) {

        Long conferenceId = conferenceIds.get(conferenceName);

        if (conferenceId == null) {
            throw new IllegalStateException("Test data refers to an unknown conference: " + conferenceName);
        }

        talks.save(Talk.create(
                conferenceId,
                title,
                description,
                speaker,
                LocalDateTime.parse(startsAt),
                room,
                level));
    }
}
