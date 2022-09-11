package uz.md.trello.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.workspace.Workspace;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 17:27
 */

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {
    @Modifying
    @Transactional
    @Query("update Workspace set deleted = true where id = :id")
    void softDeleteById(@Param("id") UUID id);

    @Transactional
    @Query("from Workspace where deleted = false")
    List<Workspace> getAllNotDeleted();

    @Transactional
    @Query("from Workspace where deleted = false and id = :id")
    Optional<Workspace> findNotDeletedById(@Param("id") UUID id);

    @Transactional
    @Query("from Workspace where deleted = false and owner.user.username = :name ")
    List<Workspace> getAllNotDeletedOfUser(@Param("name") String name);

    @Transactional
    @Query("from Workspace where deleted = false and owner.user.username = :name and :name in elements(members) ")
    List<Workspace> getAllNotDeletedForMember(@Param("name") String name);

//    @Query("FROM Workspace w INNER JOIN WorkspaceUser u ON u.workspace.id = w.id  where :userId in ")
    @Query("select u.workspace from WorkspaceUser u where u.user.id = :userId")
    Optional<List<Workspace>> findAllByUserId(UUID userId);

}
