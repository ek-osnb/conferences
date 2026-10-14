package ek.osnb.demo.conferences.conference;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conferences")
class ConferenceController {

    private final ConferenceService service;

    ConferenceController(ConferenceService service) {
        this.service = service;
    }

    @GetMapping
    List<ConferenceResponse> search(@RequestParam(name = "q", required = false) String search) {
        return service.search(search);
    }

    @GetMapping("/{id}")
    ConferenceResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }
}
