package uz.md.trello.services.board.list;

//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.config.event.EventPublisher;
import uz.md.trello.config.event.GenericEvent;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.dtos.board.list.*;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.BoardListMapper;
import uz.md.trello.repository.list.BoardListRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.Archival;
import uz.md.trello.services.board.BoardService;
import uz.md.trello.services.task.TaskService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 27/08/22 23:21
 */

@Service
@Transactional
public class BoardListService extends AbstractService<
        BoardList,
        BoardListDTO,
        BoardListCDTO,
        BoardListUDTO,
        UUID,
        BoardListMapper,
        BoardListRepository
        > implements Archival<UUID> {

    private final EventPublisher eventPublisher;

    private final BoardService boardService;
    private final TaskService taskService;
    private final Privacy privacy;
    private final BaseUtils baseUtils;

    public BoardListService(BoardListRepository repository,
                            BoardListMapper mapper,
                            EventPublisher eventPublisher,
                            BoardService boardService,
                            @Lazy TaskService taskService,
                            Privacy privacy,
                            BaseUtils baseUtils) {
        super(repository, mapper);
        this.eventPublisher = eventPublisher;
        this.boardService = boardService;
        this.taskService = taskService;
        this.privacy = privacy;
        this.baseUtils = baseUtils;
    }

    @Override
    public List<BoardListDTO> getAll() {
        return mapper.toDtos(repository.findAllWhereNotDeleted());
    }


    @Override
//    @Cacheable(value = "boardLists")
    public BoardListDTO findById(UUID uuid) {
        BoardList boardList = findEntityById(uuid);
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard().getWorkspace(),
                "Permission denied. You are not member of this workspace. ");
        if (boardList.isArchived()) throw new GenericNotFoundException("BoardList is archived", 404);
        return mapper.toDto(boardList);
    }

    @Override
    public BoardListDTO create(BoardListCDTO boardListCDTO, UUID createdBy) {
        BoardList boardList = mapper.fromCDTO(boardListCDTO);
        Board board = boardService.findEntityById(boardListCDTO.getBoardId());
        boardList.setBoard(board);
        boardList.setListOrder(baseUtils.getPositionList(board.getId()));
        return mapper.toDto(repository.save(boardList));
    }

    @Override
//    @CacheEvict(cacheNames = "boardLists")
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }


    @Override
//    @CacheEvict(cacheNames = "boardLists")
    public void softDeleteById(UUID uuid) {
        BoardList boardList = findEntityById(uuid);
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),"Permission denied. You are not member of this workspace");
        privacy.checkForArchived(boardList,"BoardList is not archived");
        repository.softDeleteById(uuid);
    }

    @Override
//    @CacheEvict(cacheNames = "boardLists")
    public void softDelete(BoardList entity) {
        softDeleteById(entity.getId());
    }

    @Override
//    @CachePut(cacheNames = "boardLists", key = "#boardListUDTO.id")
    public BoardListDTO update(BoardListUDTO boardListUDTO) {
        BoardList boardList = findEntityById(boardListUDTO.getId());
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(), "Permission denied. You are not member of this workspace");
        return mapper.toDto(mapper.fromUDTO(boardListUDTO, boardList));
    }

    @Override
//    @Cacheable(value = "boardLists", key = "#uuid")
    public BoardList findEntityById(UUID uuid) {
        return repository.findNotDeletedById(uuid).orElseThrow(() -> new GenericNotFoundException(" BoardList not found", 404));
    }

//    @Cacheable(value = "boardLists", key = "#uuid")
    public BoardList findEntityByIdNotArchived(UUID uuid) {
        BoardList list = repository.findNotDeletedById(uuid).orElseThrow(() -> new GenericNotFoundException(" BoardList not found", 404));
        if (!list.isArchived()){
            return list;
        }
        throw new GenericNotFoundException("BoardList is archived", 404);
    }

    @Override
    @PreAuthorize("isAuthenticated() and hasAuthority('WORKSPACE_READ')")
//    @CacheEvict(cacheNames = "boardLists")
    public void archiveById(UUID uuid) {
        BoardList boardList = findEntityById(uuid);
        Board board = boardList.getBoard();
        privacy.checkForMemberPermissionIfFoundGet(board,
                "Permission denied. Board is open but  Workspace is private. You are not member of this workspace");
        boardList.setArchived(true);
        boardList.setArchivedAt(LocalDateTime.now());
        repository.changePositions(boardList.getListOrder());
        repository.save(boardList);
    }


    public List<BoardListDTO> getAllOrderedByPositionOf(UUID boardId) {
        Board board = boardService.findEntityById(boardId);
        privacy.checkForMemberPermissionIfFoundGet(board,
                "Permission denied. You are not member of this workspace");
        return mapper.toDtos(repository.findAllWhereNotDeletedByBoard(board.getId()));
    }

    public BoardListDTO copyThis(BoardListCopyDTO dto) {
        BoardList boardList = findEntityById(dto.getId());
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),
                "Permission denied. You are not member of this workspace");
        BoardList boardList1 = new BoardList(boardList);
        boardList1.setName(dto.getNewName());
        return mapper.toDto(repository.save(boardList1));
    }

//    @CacheEvict(cacheNames = "boardLists")
    public void moveThis(BoardListMoveDTO moveDTO) {
        BoardList boardList = findEntityById(moveDTO.getBoardListId());

        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),
                "Permission denied. You are not member of this workspace");

        GenericEvent<BoardListMoveDTO> genericEvent = new GenericEvent<>(moveDTO, true);
        eventPublisher.publishCustomEvent(genericEvent);
    }

    public Integer getCountOfNotDeletedAndNOtArchived(UUID boardId) {
        return repository.countLists(boardId);
    }
}
