package ek.osnb.demo.conferences.registration;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/conferences/{conferenceId}")
class RegistrationController {

    private final RegistrationService service;

    RegistrationController(RegistrationService service) {
        this.service = service;
    }

    @PostMapping("/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    RegistrationResponse register(
            @PathVariable Long conferenceId,
            @Valid @RequestBody RegistrationRequest request) {

        return service.register(conferenceId, request);
    }

    @GetMapping("/availability")
    AvailabilityResponse availability(@PathVariable Long conferenceId) {
        return service.availability(conferenceId);
    }
}
