package ek.osnb.demo.conferences.registration;

import org.springframework.data.jpa.repository.JpaRepository;

interface RegistrationRepository extends JpaRepository<Registration, Long> {

    long countByConferenceId(Long conferenceId);

    boolean existsByConferenceIdAndEmail(Long conferenceId, String email);
}
