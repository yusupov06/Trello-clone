package uz.md.trello.services.task;

import org.springframework.stereotype.Service;
import uz.md.trello.domains.list.Comment;
import uz.md.trello.domains.list.Task;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.task.comment.CommentDTO;
import uz.md.trello.dtos.task.comment.CommentCDTO;
import uz.md.trello.dtos.task.comment.CommentUDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.CommentMapper;
import uz.md.trello.repository.task.CommentRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.workspace.WorkspaceUserService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 15:13
 */
@Service
public class CommentService extends AbstractService<
        Comment,
        CommentDTO,
        CommentCDTO,
        CommentUDTO,
        UUID,
        CommentMapper,
        CommentRepository
        > {


    private final TaskService taskService;
    private final BaseUtils baseUtils;
    private final WorkspaceUserService workspaceUserService;
    private final Privacy privacy;

    public CommentService(CommentRepository repository,
                          CommentMapper mapper,
                          TaskService taskService,
                          BaseUtils baseUtils, WorkspaceUserService workspaceUserService,
                          Privacy privacy) {
        super(repository, mapper);
        this.taskService = taskService;
        this.baseUtils = baseUtils;
        this.workspaceUserService = workspaceUserService;
        this.privacy = privacy;
    }

    @Override
    public List<CommentDTO> getAll() {
        return null;
    }

    public List<CommentDTO> getAllOf(UUID taskId) {
        Task task = taskService.findEntityById(taskId);
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        return mapper.toDtos(repository.findAllByTaskId(taskId).orElse(new ArrayList<>()));
    }

    @Override
    public CommentDTO findById(UUID id) {
        Comment comment = findEntityById(id);
        return mapper.toDto(comment);
    }

    @Override
    public CommentDTO create(CommentCDTO commentCDTO, UUID createdBy) {
        Task task = taskService.findEntityById(commentCDTO.getTaskId());
        privacy.checkForMemberPermissionIfFoundGet(task.getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        Comment comment = mapper.fromCDTO(commentCDTO);
        comment.setTask(task);

        WorkspaceUser workspaceUser =
                workspaceUserService.getWorkspaceUserByUserId(baseUtils.getCurrentUser().getId(),
                        task.getBoardList().getBoard().getWorkspace().getId());

        comment.setOwner(workspaceUser);
        Comment save = repository.save(comment);
        return mapper.toDto(save);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public void softDeleteById(UUID id) {
        Comment comment = findEntityById(id);
        privacy.checkForOwnerPermission(comment,
                "Permission denied. You are not owner of this comment");
        repository.softDeleteById(id);
    }

    @Override
    public void softDelete(Comment entity) {
        softDeleteById(entity.getId());
    }

    @Override
    public CommentDTO update(CommentUDTO commentUDTO) {
        Comment comment = findEntityById(commentUDTO.commentId());
        privacy.checkForMemberPermissionIfFoundGet(comment.getTask().getBoardList().getBoard(),
                "Permission denied. You are not member of this workspace");
        Comment comment1 = mapper.fromUDTO(commentUDTO, comment);
        Comment save = repository.save(comment1);
        return mapper.toDto(save);
    }

    @Override
    public Comment findEntityById(UUID id) {
        return repository.findNotDeletedById(id).orElseThrow(() -> new GenericNotFoundException("Comment not found", 404));
    }
}
