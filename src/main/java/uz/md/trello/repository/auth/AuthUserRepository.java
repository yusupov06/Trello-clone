package uz.md.trello.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.md.trello.domains.auth.AuthUser;

import java.util.Optional;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/12:09 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */
public interface AuthUserRepository extends JpaRepository<AuthUser, UUID> {
    Optional<AuthUser> findByUsername(String username);

    @Query("update AuthUser set deleted = true where id = :id")
    void softDeleteById(UUID id);
}
