package ek.osnb.demo.conferences.talk;

import ek.osnb.demo.conferences.conference.ConferenceApi;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
class TalkService {

    private final TalkRepository talks;
    private final ConferenceApi conferences;

    TalkService(TalkRepository talks, ConferenceApi conferences) {
        this.talks = talks;
        this.conferences = conferences;
    }

    List<TalkResponse> findByConference(Long conferenceId) {
        conferences.getConference(conferenceId);

        List<TalkResponse> responses = new ArrayList<>();

        for (Talk talk : talks.findByConferenceIdOrderByStartsAt(conferenceId)) {
            responses.add(TalkResponse.from(talk));
        }

        return responses;
    }
}
