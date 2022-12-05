package pl.dreilt.basicspringmvcapp.repository;

import org.springframework.data.repository.CrudRepository;
import pl.dreilt.basicspringmvcapp.entity.EventImage;

public interface EventImageRepository extends CrudRepository<EventImage, Long> {
}
