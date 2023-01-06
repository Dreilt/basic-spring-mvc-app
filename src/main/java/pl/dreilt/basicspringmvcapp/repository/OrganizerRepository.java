package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;

import java.util.List;

public interface OrganizerRepository extends PagingAndSortingRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer")
    List<Event> findEventsByOrganizer(@Param("organizer") AppUser organizer);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer AND e.city = :city")
    List<Event> findEventsByOrganizerAndCity(@Param("organizer") AppUser organizer, @Param("city") String city);
}
