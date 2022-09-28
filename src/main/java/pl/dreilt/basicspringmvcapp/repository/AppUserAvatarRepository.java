package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dreilt.basicspringmvcapp.entity.AppUserProfileImage;

public interface AppUserAvatarRepository extends CrudRepository<AppUserProfileImage, Long> {
}
