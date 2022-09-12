package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
}
