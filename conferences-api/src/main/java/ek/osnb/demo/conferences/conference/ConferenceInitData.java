package ek.osnb.demo.conferences.conference;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Order(1)
class ConferenceInitData implements CommandLineRunner {

    private final ConferenceRepository conferences;

    ConferenceInitData(ConferenceRepository conferences) {
        this.conferences = conferences;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (conferences.count() > 0) {
            return;
        }

        conference("Øredev 2026", "Malmö, Sweden", "MalmöMässan",
                "2026-11-04", "2026-11-06", 3,
                "A developer conference about the whole process of building software, "
                        + "from code and architecture to AI and data.");

        conference("Jfokus 2027", "Stockholm, Sweden", "Stockholm Waterfront Congress Centre",
                "2027-02-08", "2027-02-10", 300,
                "A developer conference with Java at its heart. The first day is for deep dives "
                        + "and hands-on labs, then two days of talks.");

        conference("JavaLand 2027", "Rust, Germany", "Europa-Park",
                "2027-03-02", "2027-03-04", 250,
                "A Java community conference held in an amusement park.");

        conference("KubeCon + CloudNativeCon Europe 2027", "Barcelona, Spain", null,
                "2027-03-15", "2027-03-18", 500,
                "The Cloud Native Computing Foundation's flagship conference: Kubernetes "
                        + "and cloud native open source.");

        conference("Devoxx France 2027", "Paris, France", "Palais des Congrès",
                "2027-04-07", "2027-04-09", 400,
                "Three days of talks for developers, from Java and the web to cloud and AI.");
    }

    private void conference(
            String name,
            String city,
            String venue,
            String startDate,
            String endDate,
            int capacity,
            String description) {

        conferences.save(Conference.create(
                name,
                city,
                venue,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate),
                description,
                capacity));
    }
}
