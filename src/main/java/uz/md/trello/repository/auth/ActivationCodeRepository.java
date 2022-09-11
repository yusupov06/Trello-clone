package uz.md.trello.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.trello.domains.auth.ActivationCode;

import java.util.Optional;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/11:23 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */
public interface ActivationCodeRepository extends JpaRepository<ActivationCode, Long> {
    Optional<ActivationCode> findByActivationCode(String code);
}
