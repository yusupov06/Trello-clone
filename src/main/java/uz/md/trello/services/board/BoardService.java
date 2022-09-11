package uz.md.trello.services.board;

//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.board.BoardCDTO;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.dtos.board.BoardUDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.exceptions.GenericRuntimeException;
import uz.md.trello.mappers.BoardMapper;
import uz.md.trello.repository.board.BoardRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.Archival;
import uz.md.trello.services.workspace.WorkspaceService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:10
 */

@Service
@Transactional
public class BoardService extends AbstractService<
        Board,
        BoardDTO,
        BoardCDTO,
        BoardUDTO,
        UUID,
        BoardMapper,
        BoardRepository
        > implements Archival<UUID> {

    private final WorkspaceService workspaceService;
    private final BaseUtils baseUtils;

    private final Privacy privacy;
    private final BoardUserService boardUserService;

    public BoardService(BoardRepository repository,
                        BoardMapper mapper,
                        WorkspaceService workspaceService,
                        BaseUtils baseUtils,
                        Privacy privacy,
                        @Lazy BoardUserService boardUserService) {
        super(repository, mapper);
        this.workspaceService = workspaceService;
        this.baseUtils = baseUtils;
        this.privacy = privacy;
        this.boardUserService = boardUserService;
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public List<BoardDTO> getAll() {
        return mapper.toDtos(repository.findAllNotDeleted());
    }


    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public List<BoardDTO> getAllBoardsOfWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceService.findEntityById(workspaceId);
        privacy.checkForMemberPermissionIfFoundGet(workspace,
                "Permission denied. You are not member of this workspace");
        List<Board> boards = repository.findAllNotPrivateAndOwnByWorkspace(workspaceId).orElse(new ArrayList<>());
        return mapper.toDtos(boards);
    }

    public List<BoardDTO> getAllBoardsOfWorkspaceByOwner(UUID workspaceId) {
        Workspace workspace = workspaceService.findEntityById(workspaceId);
        privacy.checkForMemberPermissionIfFoundGet(workspace,
                "Permission denied. You are not member of this workspace");
        String userName = baseUtils.getCurrentUserName();
        List<Board> boards = repository.findAllByWorkspaceByOwner(workspaceId, userName).orElse(new ArrayList<>());
        return mapper.toDtos(boards);
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
//    @Cacheable(value = "boards")
    public BoardDTO findById(UUID id) {
        BoardDTO boardDTO = findSimpEntityNotDeletedNotArchived(id);
        privacy.checkForMemberPermissionIfFoundGet(boardDTO.getId());

        return boardDTO;
    }

    private BoardDTO findSimpEntityNotDeletedNotArchived(UUID id) {
        return repository.findSimpDTONotDeletedNotArchived(id).orElseThrow(() -> new NotFoundException("Board not found"));
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public BoardDTO create(BoardCDTO boardCDTO, UUID createdBy) {
        Board board = mapper.fromCDTO(boardCDTO);
        UUID workspaceId = boardCDTO.getWorkspaceId();
        Workspace workspace = workspaceService.findEntityById(workspaceId);
        WorkspaceUser workspaceUser = privacy.checkForMemberPermissionIfFoundGet(workspace,
                "Permission denied. You are not member of this workspace");
        board.setWorkspace(workspace);
        board.setOwner(workspaceUser);
        BoardUser boardMember = boardUserService.setBoardMember(workspaceUser, BoardUserRole.ADMIN);
        List<BoardUser> members = board.getMembers();
        members.add(boardMember);
        return mapper.toDto(repository.save(board));
    }


    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
//    @CacheEvict(cacheNames = "boards", key = "#id")
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
//    @Cacheable(value = "boards", key = "#id")
    public Board findEntityById(UUID id) {
        Board board = repository.findNotDeletedById(id).orElseThrow(() -> new GenericNotFoundException("Board not found", 404));
        if (board.isArchived()) throw new GenericNotFoundException("Board is archived", 404);
        return board;
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
//    @CacheEvict(cacheNames = "boards", key = "#id")
    public void softDeleteById(UUID id) {
        Board board = findEntityById(id);
        privacy.checkForAdminPermission(board, "Permission denied. Board is Private. You are not owner");
        for (BoardList list : board.getLists()) {
            list.setDeleted(true);
        }
        board.setDeleted(true);
        repository.save(board);
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public void softDelete(Board entity) {
        softDeleteById(entity.getId());
    }

    @Override
    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
//    @CachePut(value = "boards", key = "#boardUDTO.id")
    public BoardDTO update(BoardUDTO boardUDTO) {
        Board entity = findEntityById(boardUDTO.getId());
        privacy.checkForAdminPermission(entity, "Permission denied. Board is Private. You are not owner");
        Board board = mapper.fromUDTO(boardUDTO, entity);
        Board save = repository.save(board);
        return mapper.toDto(save);
    }

    @Override
    @PreAuthorize("isAuthenticated() and hasAuthority('WORKSPACE_READ')")
//    @CacheEvict(cacheNames = "boards", key = "#uuid")
    public void archiveById(UUID uuid) {
        Board board = findEntityById(uuid);
        privacy.checkForAdminPermission(board, "Permission denied.  You are not member of this board");
        repository.archiveById(uuid);
    }


}
