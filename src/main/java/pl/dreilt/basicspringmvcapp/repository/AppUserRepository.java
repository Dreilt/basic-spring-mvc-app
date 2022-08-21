package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT a FROM AppUser a")
    List<AppUser> findAllAppUsers();

    @Query("SELECT a FROM AppUser a WHERE LOWER(a.firstName) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR LOWER(a.email) LIKE CONCAT('%', :searchQuery, '%')")
    List<AppUser> findAppUsersBySearch(@Param("searchQuery") String searchQuery);

    @Query("SELECT a FROM AppUser a WHERE LOWER(a.firstName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.lastName) LIKE CONCAT('%', :searchWord2, '%') " +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.lastName) LIKE CONCAT('%', :searchWord1, '%') " +
            "OR LOWER(a.email) LIKE CONCAT(:searchWord1, :searchWord2)" +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord2, '%')" +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord1, '%')" +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord2, '%')" +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord1, '%')")
    List<AppUser> findAppUsersBySearch(@Param("searchWord1") String searchWord1, @Param("searchWord2") String searchWord2);

    @Query(value = "SELECT * FROM app_user WHERE LOWER(first_name) IN :searchWords OR LOWER(last_name) IN :searchWords OR LOWER(email) IN :searchWords", nativeQuery = true)
    List<AppUser> findAppUsersBySearch(@Param("searchWords") String[] searchWords);
}
