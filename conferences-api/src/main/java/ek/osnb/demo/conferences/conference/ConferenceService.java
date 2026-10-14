package ek.osnb.demo.conferences.conference;

import ek.osnb.demo.conferences.common.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class ConferenceService implements ConferenceApi {

    private final ConferenceRepository conferences;

    ConferenceService(ConferenceRepository conferences) {
        this.conferences = conferences;
    }

    List<ConferenceResponse> search(String search) {
        String text = (search == null || search.isBlank()) ? null : search.trim();

        List<ConferenceResponse> responses = new ArrayList<>();

        for (Conference conference : conferences.search(text)) {
            responses.add(ConferenceResponse.from(conference));
        }

        return responses;
    }

    ConferenceResponse findById(Long id) {
        return ConferenceResponse.from(findConference(id));
    }

    @Override
    public ConferenceInfo getConference(Long conferenceId) {
        return ConferenceInfo.from(findConference(conferenceId));
    }

    @Override
    public List<ConferenceInfo> findAll() {
        List<ConferenceInfo> result = new ArrayList<>();

        for (Conference conference : conferences.findAll(Sort.by("startDate", "name"))) {
            result.add(ConferenceInfo.from(conference));
        }

        return result;
    }

    private Conference findConference(Long id) {
        Optional<Conference> result = conferences.findById(id);

        if (result.isEmpty()) {
            throw new NotFoundException("Conference not found: " + id);
        }

        return result.get();
    }
}
