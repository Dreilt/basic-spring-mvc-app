package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.dreilt.basicspringmvcapp.entity.AppUser;

public interface AdminRepository extends PagingAndSortingRepository<AppUser, Long>, JpaSpecificationExecutor<AppUser> {

    @Query("SELECT a FROM AppUser a")
    Page<AppUser> findAllUsers(Pageable pageable);
}
