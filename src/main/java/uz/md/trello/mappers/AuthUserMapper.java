package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.UserRegisterDTO;
import uz.md.trello.dtos.auth.AuthUserDTO;
import uz.md.trello.dtos.auth.AuthUserUDTO;

import java.util.List;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:28 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */

@Mapper(componentModel = "spring")
public interface AuthUserMapper extends uz.md.trello.mappers.Mapper {

    AuthUser fromRegisterDTO(UserRegisterDTO dto);

    @Mapping( source = "city", target = "address.city")
    @Mapping( source = "state", target = "address.state")
    @Mapping( source = "zip", target = "address.zip")
    AuthUser fromUDTO(AuthUserUDTO dto);

    AuthUser fromDTO(AuthUserDTO dto);

    AuthUserDTO toDTO(AuthUser domain);

    List<AuthUserDTO> toDTOs(List<AuthUser> entities);


}
