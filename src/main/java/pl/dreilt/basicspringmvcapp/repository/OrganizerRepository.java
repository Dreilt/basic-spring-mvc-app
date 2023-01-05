package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface OrganizerRepository extends PagingAndSortingRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime AND e.organizer = :organizer")
    List<Event> findUpcomingEventsByOrganizer(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("organizer") AppUser organizer);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime AND e.organizer = :organizer")
    List<Event> findPastEventsByOrganizer(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("organizer") AppUser organizer);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime AND e.organizer = :organizer AND e.city = :city")
    List<Event> findUpcomingEventsByOrganizerAndCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("organizer") AppUser organizer, @Param("city") String city);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime AND e.organizer = :organizer AND e.city = :city")
    List<Event> findPastEventsByOrganizerAndCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("organizer") AppUser organizer, @Param("city") String city);
}
