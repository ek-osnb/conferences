package ek.osnb.demo.conferences.conference;

import java.util.List;

public interface ConferenceApi {

    ConferenceInfo getConference(Long conferenceId);

    List<ConferenceInfo> findAll();
}
