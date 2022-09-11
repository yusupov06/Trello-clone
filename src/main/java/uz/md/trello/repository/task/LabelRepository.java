package uz.md.trello.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.md.trello.domains.list.Label;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 15:34
 */
public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query("from Label where board.id = :id")
    Optional<List<Label>> findAllByBoardId(@Param("id") UUID boardId);


}

