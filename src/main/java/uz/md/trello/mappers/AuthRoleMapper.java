package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import uz.md.trello.domains.auth.AuthRole;
import uz.md.trello.dtos.auth.AuthRoleCDTO;
import uz.md.trello.dtos.auth.AuthRoleDTO;
import uz.md.trello.dtos.auth.AuthRoleUDTO;

import java.util.List;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:52 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */
@Mapper(componentModel = "spring")
public interface AuthRoleMapper extends uz.md.trello.mappers.Mapper {
    AuthRoleDTO toDTO(AuthRole entity);

    List<AuthRoleDTO> toDTO(List<AuthRole> entities);

    AuthRole fromCreateDTO(AuthRoleCDTO dto);
    AuthRole fromDTO(AuthRoleDTO dto);

    AuthRole fromUDTO(AuthRoleUDTO authRoleUDTO);
}
