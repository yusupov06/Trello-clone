package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.WorkspaceUserDTO;
import uz.md.trello.dtos.workspace.user.WorkspaceUserCDTO;
import uz.md.trello.dtos.workspace.user.WorkspaceUserUDTO;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:35
 */
@Mapper(componentModel = "spring" , uses = {AuthUserMapper.class})
public interface WorkspaceUserMapper extends EntityMapper<
        WorkspaceUserDTO,
        WorkspaceUserCDTO,
        WorkspaceUserUDTO,
        WorkspaceUser
        > {

}
