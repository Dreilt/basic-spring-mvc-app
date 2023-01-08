package pl.dreilt.basicspringmvcapp.event;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.user.AppUser;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("SELECT DISTINCT e.city FROM Event e")
    List<String> findAllCities();

    @Query("SELECT e FROM Event e ORDER BY e.dateAndTime ASC")
    List<Event> findAllEvents();

    @Query("SELECT e FROM Event e WHERE e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findEventsByCity(String city);

    @Query("SELECT e FROM Event e WHERE :user MEMBER OF e.participants ORDER BY e.dateAndTime ASC")
    List<Event> findEventsByUser(@Param("user") AppUser user);

    @Query("SELECT e FROM Event e WHERE :user MEMBER OF e.participants AND e.city = :city ORDER BY e.dateAndTime ASC")
    List<Event> findEventsByUserAndCity(@Param("user") AppUser user, @Param("city") String city);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer")
    List<Event> findEventsByOrganizer(@Param("organizer") AppUser organizer);

    @Query("SELECT e FROM Event e WHERE e.organizer = :organizer AND e.city = :city")
    List<Event> findEventsByOrganizerAndCity(@Param("organizer") AppUser organizer, @Param("city") String city);
}
