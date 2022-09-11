package uz.md.trello.repository.list;

import org.hibernate.annotations.SQLDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.list.BoardList;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 27/08/22 23:23
 */
@Repository
public interface BoardListRepository extends JpaRepository<BoardList, UUID> {
    @Modifying
    @Query("update BoardList set deleted = true where id = :id")
    void softDeleteById(@Param("id") UUID uuid);

    @Modifying
    @Query("update BoardList set archived = true where id = :id")
    void archiveById(@Param("id") UUID uuid);

    @Query("from BoardList where deleted = false")
    List<BoardList> findAllWhereNotDeleted();

    @Query("from BoardList where board.id = :id and deleted = false and archived = false order by listOrder")
    List<BoardList> findAllWhereNotDeletedByBoard(@Param("id") UUID id);

    @Query("from BoardList where id = :id and deleted = false ")
    Optional<BoardList> findNotDeletedById(@Param("id") UUID uuid);

    @Query("from BoardList where board.id = :bId and listOrder between :from and :to order by listOrder")
    List<BoardList> findAllBetweenPositions(@Param("from") int from,@Param("to") int to,@Param("bId") UUID bId);

    @Transactional
    @Modifying
    @Query("update BoardList set listOrder = listOrder +1 where listOrder >= :f and  listOrder < :t and board.id = :id")
    void moveForward(@Param("f") int from,@Param("t") int to, UUID id);

    @Transactional
    @Modifying
    @Query("update BoardList set listOrder = listOrder -1 where listOrder > :f and  listOrder <= :t and board.id = :id ")
    void moveBack(@Param("f") int from,@Param("t") int to, UUID id);

    @Query("select count(id) from BoardList where board.id = :id and deleted = false and archived = false ")
    int findAllWhereNotDeletedAndNotArchivedByBoard(UUID id);

    @Modifying
    @Query("update BoardList set listOrder = listOrder-1 where listOrder > :p")
    void changePositions(@Param("p") Integer position);

    @Transactional
    @Modifying
    @Query("update BoardList set listOrder = :moveTo where id = :id")
    void changePosition(UUID id, int moveTo);


    @Query("select count (id) from BoardList where board.id = :boardId and archived = false and deleted = false ")
    int countLists(UUID boardId);
}
