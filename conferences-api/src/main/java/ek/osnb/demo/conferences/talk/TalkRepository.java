package ek.osnb.demo.conferences.talk;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

interface TalkRepository extends JpaRepository<Talk, Long> {

    List<Talk> findByConferenceIdOrderByStartsAt(Long conferenceId);
}
