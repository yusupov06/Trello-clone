package uz.md.trello.services.board;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.repository.board.BoardUserRepository;
import uz.md.trello.utils.Privacy;

import java.util.Objects;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 03/09/22 18:43
 */

@Service
public class BoardUserService {

    private final BoardUserRepository repository;
    private final BoardService boardService;

    private final Privacy privacy;

    public BoardUserService(BoardUserRepository repository,
                            @Lazy BoardService boardService,
                            Privacy privacy) {
        this.repository = repository;
        this.boardService = boardService;
        this.privacy = privacy;
    }

    public BoardUser getUserOrElseCreate(WorkspaceUser currentUser) {
        BoardUser boardUser = repository.findByUser(currentUser).orElse(null);
        if (Objects.isNull(boardUser)){
            return repository.save(
                    BoardUser.builder()
                            .role(BoardUserRole.MEMBER)
                            .user(currentUser)
                            .build()
            );
        }
        return boardUser;
    }

    public BoardUser setBoardMember(WorkspaceUser workspaceUser, BoardUserRole boardUserRole) {
        BoardUser user = repository.findMemberById(workspaceUser.getId(), boardUserRole).orElse(BoardUser.builder()
                .role(boardUserRole)
                .user(workspaceUser)
                .build());
        return repository.save(user);
    }


    @PreAuthorize("isAuthenticated() and hasAuthority('WORKSPACE_READ')")
    public void joinToTheBoard(UUID id) {
        Board board = boardService.findEntityById(id);
        Workspace workspace = board.getWorkspace();
        WorkspaceUser workspaceUser = privacy.checkForMemberPermissionIfFoundGet(workspace, "Permission denied. You are not member of this workspace");
        BoardUser boardUser = privacy.checkForJoinedIfFoundGet(board);
        if (Objects.nonNull(boardUser)) {
            throw new NotAllowedException("You were already joined to this board", 405);
        }
        setBoardMember(workspaceUser, BoardUserRole.OBSERVER);
    }

}

