package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dreilt.basicspringmvcapp.entity.AppUserRole;

import java.util.Optional;

public interface AppUserRoleRepository extends CrudRepository<AppUserRole, Long> {

    Optional<AppUserRole> findRoleByName(String name);
}
