package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.dtos.board.user.BoardUserDTO;
import uz.md.trello.dtos.board.user.BoardUserCDTO;
import uz.md.trello.dtos.board.user.BoardUserSimpDTO;
import uz.md.trello.dtos.board.user.BoardUserUDTO;
import uz.md.trello.services.workspace.WorkspaceUserService;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:38
 */
@Mapper(componentModel = "spring", uses = {WorkspaceUserMapper.class, WorkspaceUserService.class})
public interface BoardUserMapper extends EntityMapper<
        BoardUserDTO,
        BoardUserCDTO,
        BoardUserUDTO,
        BoardUser
        >{

    @Mapping(target = "name", expression = "java(getFullName(boardUser))")
    BoardUserSimpDTO toSimpleDTO(BoardUser boardUser);

    default String getFullName(BoardUser boardUser){
        return boardUser.getUser().getUser().getFirstName()+" "+boardUser.getUser().getUser().getLastName();
    }

}
