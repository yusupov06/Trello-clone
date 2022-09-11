package uz.md.trello.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.md.trello.domains.list.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 15:15
 */
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query("from Comment where task.id = :id and deleted = false ")
    Optional<List<Comment>> findAllByTaskId(@Param("id") UUID taskId);

    @Query("from Comment where id = :id and deleted = false ")
    Optional<Comment> findNotDeletedById(@Param("id") UUID id);

    @Modifying
    @Query("update Comment set deleted = true where id = :id")
    void softDeleteById(@Param("id") UUID id);
}
