package ek.osnb.demo.conferences.conference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface ConferenceRepository extends JpaRepository<Conference, Long> {

    @Query("""
            select c from Conference c
            where :search is null
               or lower(c.name) like lower(concat('%', :search, '%'))
               or lower(c.city) like lower(concat('%', :search, '%'))
            order by c.startDate, c.name
            """)
    List<Conference> search(String search);
}
