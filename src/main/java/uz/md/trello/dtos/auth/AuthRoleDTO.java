package uz.md.trello.dtos.auth;

import lombok.Builder;
import lombok.Data;
import uz.md.trello.domains.auth.AuthPermission;
import uz.md.trello.dtos.DTO;

import java.util.Collection;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:49 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */
@Data
@Builder
public class AuthRoleDTO implements DTO {
    private Long id;
    private final String code;
    private final String name;
    private Collection<AuthPermission> permissions;
}

