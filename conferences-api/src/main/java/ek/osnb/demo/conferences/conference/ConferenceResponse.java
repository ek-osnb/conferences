package ek.osnb.demo.conferences.conference;

import java.time.LocalDate;

record ConferenceResponse(
        Long id,
        String name,
        String city,
        String venue,
        LocalDate startDate,
        LocalDate endDate,
        String description,
        int capacity
) {

    static ConferenceResponse from(Conference conference) {
        return new ConferenceResponse(
                conference.getId(),
                conference.getName(),
                conference.getCity(),
                conference.getVenue(),
                conference.getStartDate(),
                conference.getEndDate(),
                conference.getDescription(),
                conference.getCapacity()
        );
    }
}
