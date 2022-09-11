package uz.md.trello.services.task;

import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.config.event.EventPublisher;
import uz.md.trello.config.event.GenericEvent;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.list.Label;
import uz.md.trello.domains.list.Task;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.AddLabelRequest;
import uz.md.trello.dtos.task.*;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.TaskMapper;
import uz.md.trello.repository.task.TaskRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.Archival;
import uz.md.trello.services.board.BoardService;
import uz.md.trello.services.board.BoardUserService;
import uz.md.trello.services.board.list.BoardListService;
import uz.md.trello.services.workspace.WorkspaceUserService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 12:02
 */
@Service
@Transactional
public class TaskService extends AbstractService<
        Task,
        TaskDTO,
        TaskCDTO,
        TaskUDTO,
        UUID,
        TaskMapper,
        TaskRepository
        > implements Archival<UUID> {

    private final BoardListService boardListService;
    private final BoardService boardService;
    private final Privacy privacy;
    private final LabelService labelService;
    private final EventPublisher eventPublisher;
    private final BaseUtils baseUtils;
    private final BoardUserService boardUserService;
    private final WorkspaceUserService workspaceUserService;

    public TaskService(TaskRepository repository,
                       TaskMapper mapper,
                       BoardListService boardListService,
                       BoardService boardService,
                       Privacy privacy,
                       LabelService labelService,
                       EventPublisher eventPublisher,
                       BaseUtils baseUtils, BoardUserService boardUserService,
                       WorkspaceUserService workspaceUserService) {
        super(repository, mapper);
        this.boardListService = boardListService;
        this.boardService = boardService;
        this.privacy = privacy;
        this.labelService = labelService;
        this.eventPublisher = eventPublisher;
        this.baseUtils = baseUtils;
        this.boardUserService = boardUserService;
        this.workspaceUserService = workspaceUserService;
    }

    @Override
    public List<TaskDTO> getAll() {
        return mapper.toDtos(repository.findAllNotDeleted());
    }


    public List<TaskDTO> getAllOfList(UUID listId) {
        BoardList boardList = boardListService.findEntityById(listId);
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard().getWorkspace(),
                "Permission denied. Workspace is private");
        List<Task> tasks = repository.findAllNotDeletedOfList(listId).orElse(new ArrayList<>());
        return mapper.toDtos(tasks);
    }

    @Override
    @Cacheable(value = "tasks", key = "#uuid")
    public TaskDTO findById(UUID uuid) {
        Task task = findEntityById(uuid);
        if (task.isArchived()) throw new GenericNotFoundException("Task is archived", 404);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard().getWorkspace(),
                "Permission denied. You are not member of this workspace");
        return mapper.toDto(task);
    }

    @Override
    public TaskDTO create(@NotNull TaskCDTO taskCDTO, UUID createdBy) {
        BoardList boardList = boardListService.findEntityById(taskCDTO.getBoardListId());
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),
                "Permission denied. You are not member of this workspace");
        Task task = mapper.fromCDTO(taskCDTO);
        task.setBoardList(boardList);
        task.setPosition(baseUtils.getPositionTask(boardList.getId()));
        Task save = repository.save(task);
        return mapper.toDto(save);
    }


    @Override
//    @CacheEvict(cacheNames = "tasks")
    public void deleteById(UUID uuid) {
        repository.deleteById(uuid);
    }

    @Override
//    @CacheEvict(cacheNames = "tasks")
    public void softDeleteById(UUID uuid) {
        Task task = findEntityById(uuid);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");

        privacy.checkForArchived(task, "Task is not archived");
        repository.softDeleteById(uuid);
    }

    @Override
//    @CacheEvict(cacheNames = "tasks")
    public void softDelete(Task entity) {
        softDeleteById(entity.getId());
    }

    @Override
//    @CachePut(cacheNames = "tasks", key = "#taskUDTO.id")
    public TaskDTO update(TaskUDTO taskUDTO) {
        Task task = findEntityById(taskUDTO.getId());
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        return mapper.toDto(mapper.fromUDTO(taskUDTO, task));
    }

    @Override
//    @Cacheable(value = "tasks", key = "#uuid")
    public Task findEntityById(UUID uuid) {
        return repository.findNotDeletedById(uuid).orElseThrow(() -> new GenericNotFoundException("Task not found", 404));
    }

////    @Cacheable(value = "tasks", key = "#uuid")
    public Task findEntityByIdNotArchived(UUID uuid) {
        Task task = repository.findNotDeletedById(uuid).orElseThrow(() -> new GenericNotFoundException("Task not found", 404));
        if (!task.isArchived()){
            return task;
        }
        throw new GenericNotFoundException("Task is archived", 404);
    }



    @Override
//    @CacheEvict(cacheNames = "tasks")
    public void archiveById(UUID uuid) {
        Task task = findEntityById(uuid);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        repository.changePositionsBack(task.getPosition(), task.getBoardList().getId());
        repository.archiveById(uuid);
    }

    public void copy(TaskCopyDTO copyDTO) {
        Task task = findEntityById(copyDTO.getTaskId());
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        GenericEvent<TaskCopyDTO> genericEvent = new GenericEvent<>(copyDTO, true);
        eventPublisher.publishCustomEvent(genericEvent);
    }

//    @CacheEvict(cacheNames = "tasks")
    public void move(TaskMoveDTO moveDTO) {
        Task task = findEntityById(moveDTO.getTaskId());
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        GenericEvent<TaskMoveDTO> genericEvent = new GenericEvent<>(moveDTO, true);
        eventPublisher.publishCustomEvent(genericEvent);
    }


//    @CachePut(cacheNames = "tasks", key = "#taskId")
    public TaskDTO addLabel(AddLabelRequest request) {
        System.out.println(" ================================sdf=================== " );
        System.out.println(request.labelId());
        System.out.println(request.taskId());
        UUID taskId = request.taskId();
        Long labelId = request.labelId();
        Task task = findEntityById(taskId);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        List<Label> labels = task.getLabels();
        Label label = labelService.findEntityById(labelId);
        privacy.checkForExistedLabel(label, labels);
        labels.add(label);
        task.setLabels(labels);
        return mapper.toDto(repository.save(task));
    }


    public void join(UUID taskId) {
        Task task = findEntityById(taskId);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        List<BoardUser> members = new ArrayList<>(task.getMembers().stream().toList());
        WorkspaceUser currentUser = workspaceUserService.getCurrentUser(task.getBoardList().getBoard().getWorkspace());
        BoardUser boardUser = boardUserService.getUserOrElseCreate(currentUser);
        members.add(boardUser);
        task.setMembers(members);
        repository.save(task);
    }

    public int getCountOfNotDeletedAndNOtArchived(UUID listId) {
        return repository.countTasks(listId);
    }
}








/**
    int from = listCurrentPosition, to = moveTo;
        if (from == to) return;

                System.out.println("from = " + from);
                System.out.println("to = " + to);

                List<BoardList> allBetweenPositions = new ArrayList<>();

        if (from < to) {

        allBetweenPositions = boardListRepository
        .findAllBetweenPositions(from, to, boardId);

        allBetweenPositions.get(0).setListOrder(to);
        int k = from;
        for (int i = 1; i < allBetweenPositions.size(); i++) {
        allBetweenPositions.get(i).setListOrder(k++);
        }

        for (BoardList b : allBetweenPositions) {
        System.out.println(" ================ ");
        System.out.println(b.getListOrder() + " " + b.getName());
        }

        } else {

        allBetweenPositions = boardListRepository
        .findAllBetweenPositions(to, from, boardId);

        allBetweenPositions.get(allBetweenPositions.size() - 1).setListOrder(to);
        for (int i = 0; i < allBetweenPositions.size() - 1; i++) {
        allBetweenPositions.get(i).setListOrder(allBetweenPositions.get(i).getListOrder() + 1);
        }
        for (BoardList b : allBetweenPositions) {
        System.out.println(" ================ ");
        System.out.println(b.getListOrder() + " " + b.getName());
        }
        }

        boardListRepository.saveAll(allBetweenPositions);


*/






























