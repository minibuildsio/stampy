package co.uk.howardpaget.stampy.repository;

import co.uk.howardpaget.stampy.entity.StampyUserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<StampyUserEntity, Long> {

  @Query(value = "select * from stampyuser where username = :username and password_hash = crypt(:password, password_hash)", nativeQuery = true)
  Optional<StampyUserEntity> findUser(String username, String password);

  boolean existsByUsername(String username);

}
