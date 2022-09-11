package uz.md.trello.mappers;

import org.apache.catalina.LifecycleState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.jpa.repository.Query;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.dtos.board.BoardCDTO;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.dtos.board.BoardSentDTO;
import uz.md.trello.dtos.board.BoardUDTO;
import uz.md.trello.repository.workspace.WorkspaceRepository;
import uz.md.trello.services.workspace.WorkspaceService;

import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:13
 */

@Mapper(componentModel = "spring", uses = {BoardUserMapper.class, BoardListMapper.class}, imports = {WorkspaceRepository.class})
public interface BoardMapper extends uz.md.trello.mappers.Mapper, EntityMapper<
        BoardDTO,
        BoardCDTO,
        BoardUDTO,
        Board
        > {

    BoardDTO toDto(Board entity);

    List<BoardSentDTO> toSendDTOs(List<Board> boards);

    BoardSentDTO toSendDTO(Board board);

}
