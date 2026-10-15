package ek.osnb.demo.conferences.registration;

import ek.osnb.demo.conferences.common.InvalidInputException;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Locale;

@Entity
@Table(
        name = "registrations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"conference_id", "email"})
)
class Registration {

    static final int NAME_MAX_LENGTH = 100;
    static final int EMAIL_MAX_LENGTH = 254;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long conferenceId;

    @Column(nullable = false, length = NAME_MAX_LENGTH)
    private String name;

    @Column(nullable = false, length = EMAIL_MAX_LENGTH)
    private String email;

    @Column(nullable = false)
    private Instant registeredAt;

    protected Registration() {
    }

    private Registration(Long conferenceId, String name, String email, Instant registeredAt) {
        if (conferenceId == null) {
            throw new InvalidInputException("A registration must have a conference");
        }
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("A registration must have a name");
        }
        if (email == null || email.isBlank()) {
            throw new InvalidInputException("A registration must have an email address");
        }

        this.conferenceId = conferenceId;
        this.name = name.trim();
        this.email = normalizeEmail(email);
        this.registeredAt = registeredAt;
    }

    static Registration create(Long conferenceId, String name, String email, Instant registeredAt) {
        return new Registration(conferenceId, name, email, registeredAt);
    }

    static String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }

    Long getId() {
        return id;
    }

    Long getConferenceId() {
        return conferenceId;
    }

    String getName() {
        return name;
    }

    String getEmail() {
        return email;
    }

    Instant getRegisteredAt() {
        return registeredAt;
    }
}
