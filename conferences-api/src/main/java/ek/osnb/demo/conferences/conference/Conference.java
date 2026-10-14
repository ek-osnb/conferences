package ek.osnb.demo.conferences.conference;

import ek.osnb.demo.conferences.common.InvalidInputException;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "conferences")
class Conference {

    static final int NAME_MAX_LENGTH = 100;
    static final int CITY_MAX_LENGTH = 60;
    static final int VENUE_MAX_LENGTH = 100;
    static final int DESCRIPTION_MAX_LENGTH = 1000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, length = CITY_MAX_LENGTH)
    private String city;

    @Column(length = VENUE_MAX_LENGTH)
    private String venue;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(length = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(nullable = false)
    private int capacity;

    protected Conference() {
    }

    private Conference(
            String name,
            String city,
            String venue,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            int capacity) {

        if (name == null || name.isBlank()) {
            throw new InvalidInputException("A conference must have a name");
        }
        if (city == null || city.isBlank()) {
            throw new InvalidInputException("A conference must have a city");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidInputException("A conference must have a start date and an end date");
        }
        if (endDate.isBefore(startDate)) {
            throw new InvalidInputException("A conference cannot end before it starts");
        }
        if (capacity < 1) {
            throw new InvalidInputException("A conference must have room for at least 1 person");
        }

        this.name = name.trim();
        this.city = city.trim();
        this.venue = venue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.capacity = capacity;
    }

    static Conference create(
            String name,
            String city,
            String venue,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            int capacity) {

        return new Conference(name, city, venue, startDate, endDate, description, capacity);
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getCity() {
        return city;
    }

    String getVenue() {
        return venue;
    }

    LocalDate getStartDate() {
        return startDate;
    }

    LocalDate getEndDate() {
        return endDate;
    }

    String getDescription() {
        return description;
    }

    int getCapacity() {
        return capacity;
    }
}
