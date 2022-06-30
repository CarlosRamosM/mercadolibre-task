package mx.com.ml.rebell.alliance.security.dm.repository;

import mx.com.ml.rebell.alliance.security.dm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  /**
   * Searches for user information by username.
   * @param username User
   * @return User information.
   */
  Optional<User> findByUsernameIgnoreCase(final String username);
}
