package ek.osnb.demo.conferences.talk;

import java.time.LocalDateTime;

record TalkResponse(
        Long id,
        String title,
        String description,
        String speaker,
        LocalDateTime startsAt,
        String room,
        Level level
) {

    static TalkResponse from(Talk talk) {
        return new TalkResponse(
                talk.getId(),
                talk.getTitle(),
                talk.getDescription(),
                talk.getSpeaker(),
                talk.getStartsAt(),
                talk.getRoom(),
                talk.getLevel()
        );
    }
}
