package ek.osnb.demo.conferences.talk;

import ek.osnb.demo.conferences.common.InvalidInputException;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "talks")
class Talk {

    static final int TITLE_MAX_LENGTH = 150;
    static final int DESCRIPTION_MAX_LENGTH = 1000;
    static final int SPEAKER_MAX_LENGTH = 100;
    static final int ROOM_MAX_LENGTH = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long conferenceId;

    @Column(nullable = false, length = TITLE_MAX_LENGTH)
    private String title;

    @Column(length = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(nullable = false, length = SPEAKER_MAX_LENGTH)
    private String speaker;

    @Column(nullable = false)
    private LocalDateTime startsAt;

    @Column(length = ROOM_MAX_LENGTH)
    private String room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Level level;

    protected Talk() {
    }

    private Talk(
            Long conferenceId,
            String title,
            String description,
            String speaker,
            LocalDateTime startsAt,
            String room,
            Level level) {

        if (conferenceId == null) {
            throw new InvalidInputException("A talk must belong to a conference");
        }
        if (title == null || title.isBlank()) {
            throw new InvalidInputException("A talk must have a title");
        }
        if (speaker == null || speaker.isBlank()) {
            throw new InvalidInputException("A talk must have a speaker");
        }
        if (startsAt == null) {
            throw new InvalidInputException("A talk must have a start time");
        }
        if (level == null) {
            throw new InvalidInputException("A talk must have a level");
        }

        this.conferenceId = conferenceId;
        this.title = title.trim();
        this.description = description;
        this.speaker = speaker.trim();
        this.startsAt = startsAt;
        this.room = room;
        this.level = level;
    }

    static Talk create(
            Long conferenceId,
            String title,
            String description,
            String speaker,
            LocalDateTime startsAt,
            String room,
            Level level) {

        return new Talk(conferenceId, title, description, speaker, startsAt, room, level);
    }

    Long getId() {
        return id;
    }

    Long getConferenceId() {
        return conferenceId;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    String getSpeaker() {
        return speaker;
    }

    LocalDateTime getStartsAt() {
        return startsAt;
    }

    String getRoom() {
        return room;
    }

    Level getLevel() {
        return level;
    }
}
