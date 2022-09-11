package uz.md.trello.repository.workspace;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.domains.workspace.WorkspaceUser;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:56
 */
@Repository
public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, UUID> {


    @Query("from WorkspaceUser where user.username  = :name")
    Optional<WorkspaceUser> findMemberByUsername(@Param("name") String name);

    @Query("from WorkspaceUser where user.id = :id and role = :role")
    Optional<WorkspaceUser> findMemberByIdAndRole(UUID id, Role role);

    @Query("from WorkspaceUser where id = :id and user.deleted = false ")
    Optional<WorkspaceUser> findNotDeletedById(@Param("id") UUID id);

    @Modifying
    @Transactional
    @Query("update WorkspaceUser set role = :role where id = :id")
    void changeUserRole(UUID id, Role role);

    @Query("from WorkspaceUser where workspace.id = :wId and role = 'ADMIN' and id <> :uId")
    Optional<WorkspaceUser> findUserExceptThisWhichIsAdmin(@Param("wId") UUID workspaceId,@Param("uId") UUID changingId);

    @Query("from WorkspaceUser where user.id = :id and workspace.id = :wId")
    Optional<WorkspaceUser> findUserByUserIDAndWorkspaceId(UUID id,@Param("wId") UUID workspaceId);

//    @Query("SELECT CASE WHEN w. THEN true ELSE false END FROM WorkspaceUser w WHERE w.user.username = :userName")
//    boolean checkForMember(UUID boardId, String userName);


//    Optional<WorkspaceUser> findByWorkspaceId(UUID id);
}
