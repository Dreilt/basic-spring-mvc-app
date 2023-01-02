package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.entity.AppUser;
import pl.dreilt.basicspringmvcapp.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime ORDER BY e.dateAndTime ASC")
    List<Event> findAllUpcomingEvents(@Param("currentDateAndTime") LocalDateTime currentDateAndTime);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime ORDER BY e.dateAndTime ASC")
    List<Event> findAllPastEvents(@Param("currentDateAndTime") LocalDateTime currentDateAndTime);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime AND e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findUpcomingEventsByCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("city") String city);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime AND e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findPastEventsByCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("city") String city);

    @Query("SELECT DISTINCT e.city FROM Event e")
    List<String> findAllCities();

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime AND :user MEMBER OF e.participants ORDER BY e.dateAndTime ASC")
    List<Event> findUpcomingEventsByUser(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("user") AppUser user);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime AND :user MEMBER OF e.participants ORDER BY e.dateAndTime ASC")
    List<Event> findPastEventsByUser(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("user") AppUser user);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime > :currentDateAndTime AND :user MEMBER OF e.participants AND e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findUpcomingEventsByUserAndCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("user") AppUser user, @Param("city") String city);

    @Query("SELECT e FROM Event e WHERE e.dateAndTime < :currentDateAndTime AND :user MEMBER OF e.participants AND e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findPastEventsByUserAndCity(@Param("currentDateAndTime") LocalDateTime currentDateAndTime, @Param("user") AppUser user, @Param("city") String city);
}
