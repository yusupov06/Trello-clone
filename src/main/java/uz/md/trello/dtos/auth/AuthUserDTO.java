package uz.md.trello.dtos.auth;

import lombok.*;
import uz.md.trello.domains.auth.Address;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.DTO;

import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/10:48 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUserDTO implements DTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private Address address;
    private String phone;
    private String username;
    private String email;
    private AuthUser.Status status;
    private LocalDateTime lastLoginTime;
}
