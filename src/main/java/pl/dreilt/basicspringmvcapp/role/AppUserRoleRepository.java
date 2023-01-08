package pl.dreilt.basicspringmvcapp.role;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppUserRoleRepository extends CrudRepository<AppUserRole, Long> {

    Optional<AppUserRole> findRoleByName(String name);
}
