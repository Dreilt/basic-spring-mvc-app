package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Query("SELECT a FROM AppUser a")
    Page<AppUser> findAllAppUsers(Pageable pageable);

    @Query("SELECT a FROM AppUser a WHERE LOWER(a.firstName) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchQuery, '%') " +
            "OR LOWER(a.email) LIKE CONCAT('%', :searchQuery, '%')")
    Page<AppUser> findAppUsersBySearch(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT a FROM AppUser a WHERE LOWER(a.firstName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.lastName) LIKE CONCAT('%', :searchWord2, '%') " +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.lastName) LIKE CONCAT('%', :searchWord1, '%') " +
            "OR LOWER(a.email) LIKE CONCAT(:searchWord1, :searchWord2)" +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord2, '%')" +
            "OR LOWER(a.firstName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord1, '%')" +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchWord1, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord2, '%')" +
            "OR LOWER(a.lastName) LIKE CONCAT('%', :searchWord2, '%') AND LOWER(a.email) LIKE CONCAT('%', :searchWord1, '%')")
    Page<AppUser> findAppUsersBySearch(@Param("searchWord1") String searchWord1, @Param("searchWord2") String searchWord2, Pageable pageable);

    @Query(value = "SELECT * FROM app_user WHERE LOWER(first_name) IN :searchWords OR LOWER(last_name) IN :searchWords OR LOWER(email) IN :searchWords", nativeQuery = true)
    Page<AppUser> findAppUsersBySearch(@Param("searchWords") String[] searchWords, Pageable pageable);

    @Query("SELECT a FROM AppUser a WHERE a.email = :username")
    AppUser findAppUserByUsername(String username);
}
