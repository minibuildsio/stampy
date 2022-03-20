package co.uk.howardpaget.stampy.repository;

import co.uk.howardpaget.stampy.entity.StampEntity;
import org.springframework.data.repository.CrudRepository;

public interface StampRepository extends CrudRepository<StampEntity, Long> {
}
