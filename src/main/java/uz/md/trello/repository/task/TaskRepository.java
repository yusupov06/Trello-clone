package uz.md.trello.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.md.trello.domains.list.Task;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 17:28
 */
public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("from Task where deleted = false")
    List<Task> findAllNotDeleted();

    @Query("from Task where boardList.id = :listId and deleted = false and archived = false")
    Optional<List<Task>> findAllNotDeletedOfList(UUID listId);

    @Modifying
    @Query("update Task set deleted  = true where id = :id")
    void softDeleteById(@Param("id") UUID uuid);

    @Modifying
    @Query("update Task set deleted = true where id = :id")
    void archiveById(@Param("id") UUID uuid);

    @Query("from Task where id = :id")
    Optional<Task> findNotDeletedById(@Param("id") UUID uuid);

    @Transactional
    @Modifying
    @Query("update Task set position = position -1 where position > :f and  position <= :t and boardList.id = :bId")
    void moveBack(@Param("f") int from, @Param("t") int to, @Param("bId") UUID boardListId);

    @Query("from Task where boardList.id = :lId and position between :from and :to order by position")
    List<Task> findAllBetweenPositions(@Param("from") int from, @Param("to") int to, @Param("lId") UUID listId);

    @Query("select count(id) from Task where boardList.id = :id and deleted = false and archived = false ")
    int findAllWhereNotDeletedAndNotArchivedByBoard(UUID id);

    @Modifying
    @Query("update Task set position = position-1 where boardList.id = :lId and position > :p")
    void changePositionsBack(@Param("p") Integer position, @Param("lId") UUID listId);

    @Modifying
    @Query("update Task set position = position+1 where boardList.id = :lId and position > :p")
    void changePositionsForward(@Param("p") Integer position, @Param("lId") UUID listId);

    @Modifying
    @Transactional
    @Query("update Task set position = :to where id = :id")
    void changePosition(UUID id, int to);

    @Transactional
    @Modifying
    @Query("update Task set position = position +1 where position >= :f and  position < :t and boardList.id = :id")
    void moveForward(@Param("f") int from,@Param("t") int to, UUID id);

    @Query("select count (id) from Task where boardList.id = :listId and deleted = false and archived = false ")
    int countTasks(UUID listId);
}
