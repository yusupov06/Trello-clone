package uz.md.trello.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.board.BoardUserRole;
import uz.md.trello.domains.workspace.WorkspaceUser;

import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:11
 */
@Repository
public interface BoardUserRepository extends JpaRepository<BoardUser, UUID> {

    @Query("from BoardUser where user.role = 'ADMIN' and id = :id")
    Optional<BoardUser> findAdminById(@Param("id") UUID id);

    @Query("from BoardUser where user.id = :id and  role = :role")
    Optional<BoardUser> findMemberById(@Param("id") UUID id, @Param("role") BoardUserRole boardUserRole);

    @Query("from BoardUser  where user = :user")
    Optional<BoardUser> findByUser(@Param("user") WorkspaceUser currentUser);

    @Query("SELECT CASE WHEN b.board.id = :boardId THEN true ELSE false END FROM BoardUser b WHERE b.user.user.username = :userName")
    boolean checkForMember(UUID boardId, String userName);
}
