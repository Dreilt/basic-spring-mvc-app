package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dreilt.basicspringmvcapp.entity.ProfileImage;

public interface ProfileImageRepository extends CrudRepository<ProfileImage, Long> {
}
