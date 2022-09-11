package uz.md.trello.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.md.trello.domains.auth.AuthRole;

import java.util.Optional;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:51 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */
public interface AuthRoleRepository extends JpaRepository<AuthRole, Long> {

    Optional<AuthRole> findByName(String name);

    @Query("update AuthRole set deleted = true where id = :id")
    void softDeleteById(Long id);
}
