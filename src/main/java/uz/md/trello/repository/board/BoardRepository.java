package uz.md.trello.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.board.Board;
import uz.md.trello.dtos.board.BoardDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 17:29
 */
public interface BoardRepository extends JpaRepository<Board, UUID>{

    @Modifying
    @Transactional
    @Query("update Board set deleted = true where id = :id")
    void softDeleteById(@Param("id") UUID id);

    @Query("from Board where workspace.id = :id")
    Optional<List<Board>> findAllByWorkspace(@Param("id") UUID id);

    @Query("from Board where (workspace.id = :id and visibility <> 'PRIVATE') and deleted = false ")
    Optional<ArrayList<Board>> findAllNotPrivateAndOwnByWorkspace(@Param("id") UUID workspaceId);

    @Modifying
    @Query("update Board set archived = true where id = :id")
    Optional<Void> archiveById(@Param("id") UUID uuid);

    @Query("from Board where deleted = false")
    List<Board> findAllNotDeleted();

    @Query("from  Board where id = :id and  deleted = false ")
    Optional<Board> findNotDeletedById(@Param("id") UUID id);

    /**
     * @param id ->
     * @return  simple dto
     */

    @Query("select b.id,  b.boardTitle, b.description, b.createdAt, b.updatedAt, b.visibility from Board b  where b.id = :id and b.deleted = false  and b.archived = false ")
    Optional<BoardDTO> findSimpDTONotDeletedNotArchived(@Param("id") UUID id);

    @Query("from Board where workspace.id = :wId and owner.user.username = :username and deleted = false ")
    Optional<List<Board>> findAllByWorkspaceByOwner(@Param("wId") UUID wId,@Param("username") String username);
}
