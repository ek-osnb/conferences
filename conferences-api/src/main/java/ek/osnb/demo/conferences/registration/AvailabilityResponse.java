package ek.osnb.demo.conferences.registration;

record AvailabilityResponse(
        Long conferenceId,
        int capacity,
        long registered,
        long seatsLeft
) {
}
