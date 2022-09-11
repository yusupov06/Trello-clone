package uz.md.trello.utils;


import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.board.BoardVisibility;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.list.Comment;
import uz.md.trello.domains.list.Label;
import uz.md.trello.domains.list.Task;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.Invitation;
import uz.md.trello.exceptions.InvalidPositionException;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.repository.board.BoardUserRepository;
import uz.md.trello.repository.workspace.WorkspaceUserRepository;
import uz.md.trello.services.workspace.WorkspaceUserService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 08:46
 */
@Component
public class Privacy {

    private final BaseUtils baseUtils;
    private final WorkspaceUserService workspaceUserService;
    private final WorkspaceUserRepository workspaceUserRepository;
    private final BoardUserRepository boardUserRepository;

    public Privacy(BaseUtils baseUtils,
                   @Lazy WorkspaceUserService workspaceUserService,
                   WorkspaceUserRepository workspaceUserRepository,
                   BoardUserRepository boardUserRepository) {
        this.baseUtils = baseUtils;
        this.workspaceUserService = workspaceUserService;
        this.workspaceUserRepository = workspaceUserRepository;
        this.boardUserRepository = boardUserRepository;
    }

    public WorkspaceUser checkForMemberPermissionIfFoundGet(Workspace workspace, String error) {
        String userName = baseUtils.getCurrentUserName();
        return getWorkspaceUser(workspace, userName, error);
    }

    //    @Cacheable(value = "workspaceUser", key = "#workspace.id")
    public WorkspaceUser getWorkspaceUser(@NonNull Workspace workspace, String userName, String error) {
        return workspace.getMembers().stream()
                .filter(workspaceUser -> workspaceUser.getUser().getUsername().equals(userName))
                .findFirst().orElseThrow(() -> new NotAllowedException(error, 405));
    }

    public WorkspaceUser checkForMemberPermissionIfFoundGet(@NonNull Board board, String error) {
        String userName = baseUtils.getCurrentUserName();
        if (board.getVisibility().equals(BoardVisibility.PRIVATE)) {
            if (!board.getOwner().getUser().getUsername().equals(userName)) {
                throw new NotAllowedException("Permission denied. Board is private. You are not owner of this board", 405);
            } else return board.getOwner();
        }
        return getWorkspaceUser(board.getWorkspace(), userName, error);
    }

    public void checkForMemberPermissionIfFoundGet(@NonNull UUID boardId) {
        String userName = baseUtils.getCurrentUserName();
        if (!boardUserRepository.checkForMember(boardId, userName)) {
            throw new NotAllowedException("Permission denied. Board is open but  Workspace is private. You are not member of this workspace", 405);
        }
    }


    public void checkForAdminPermission(Workspace workspace) {
        WorkspaceUser workspaceUser = workspaceUserService.getCurrentUser(workspace);
        checkForAdmin(workspace, workspaceUser);
    }

    public void checkForOwnerPermission(Workspace workspace) {
        WorkspaceUser workspaceUser = workspaceUserService.getCurrentUser(workspace);
        if (!workspace.getOwner().getId().equals(workspaceUser.getId())) {
            throw new NotAllowedException("Permission denied. You are not Owner of this workspace", 405);
        }
    }

    public void checkForAdmin(@NonNull Workspace workspace, @NonNull WorkspaceUser workspaceUser) {

        if (!workspaceUser.getWorkspace().getId().equals(workspace.getId())) {
            throw new NotAllowedException("Permission denied. You are not member of this workspace", 405);
        }

        if (!workspaceUser.getRole().equals(Role.ADMIN)) {
            throw new NotAllowedException("Permission denied. You are not ADMIN", 405);
        }

    }

    public void checkForAdminPermission(@NonNull Board board,@NonNull String error) {
        String userName = baseUtils.getCurrentUserName();
        for (BoardUser member : board.getMembers()) {
            if (member.getUser().getUser().getUsername().equals(userName)) {
                if (!member.getRole().equals(BoardUserRole.ADMIN)) {
                    throw new NotAllowedException(error, 405);
                }
            }
        }
    }

    public void checkForOwnerPermission(@NonNull Comment comment,@NonNull String error) {
        String userName = baseUtils.getCurrentUserName();
        if (comment.getTask().getBoardList().getBoard().getOwner().getUser().getUsername().equals(userName))
            return;
        if (!comment.getOwner().getUser().getUsername().equals(userName)) {
            throw new NotAllowedException(error, 405);
        }
    }


    public BoardUser checkForJoinedIfFoundGet(@NonNull Board board) {
        String userName = baseUtils.getCurrentUserName();
        return board.getMembers().stream()
                .filter(boardUser -> boardUser.getUser().getUser().getUsername().equals(userName))
                .findFirst().orElse(null);
    }

    public void checkForValidPositionForList(Integer position, UUID boardId) {
        if (position < 1 || position > BaseUtils.boardPositionForLists.get(boardId)) {
            throw new InvalidPositionException("Out of bounds with position %s".formatted(position), 406);
        }
    }

    public void checkForValidPositionForTask(Integer position, UUID listId) {
        if (position < 1 || position > BaseUtils.positionForTasks.get(listId)) {
            throw new InvalidPositionException("Out of bounds with position %s".formatted(position), 406);
        }
    }

    public void checkForArchived(@NonNull BoardList boardList,@NonNull String error) {
        if (!boardList.isArchived()) {
            throw new NotAllowedException(error, 405);
        }
    }

    public void checkForArchived(@NonNull Task task,@NonNull String error) {
        if (!task.isArchived()) {
            throw new NotAllowedException(error, 405);
        }
    }

    public void checkForExistedLabel(@NonNull Label label,@NonNull List<Label> labels) {
        Label label2 = labels.stream().filter(label1 -> label1.getId().equals(label.getId())).findFirst().orElse(null);
        if (Objects.nonNull(label2)) {
            throw new NotAllowedException("This label is existed in this task", 405);
        }
    }

    public void checkForIsAuthenticatedUser(@NonNull Invitation invitation) {
        AuthUser currentUser = baseUtils.getCurrentUser();
        if (!currentUser.getId().equals(invitation.getReceiver().getId())) {
            throw new NotAllowedException("Permission denied. You are not invited user", 405);
        }
    }

    public void checkForMember(Workspace workspace, WorkspaceUser changing) {
        if (!changing.getWorkspace().getId().equals(workspace.getId())) {
            throw new NotAllowedException("Permission denied. You are not member of this workspace", 405);
        }
    }

}
