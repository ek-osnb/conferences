package ek.osnb.demo.conferences.conference;

public record ConferenceInfo(
        Long id,
        String name,
        int capacity
) {

    static ConferenceInfo from(Conference conference) {
        return new ConferenceInfo(conference.getId(), conference.getName(), conference.getCapacity());
    }
}
