package uz.md.trello.services.workspace;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.ChangeUserRoleRequest;
import uz.md.trello.dtos.workspace.InvitationRequest;
import uz.md.trello.dtos.workspace.RemoveUserRequest;
import uz.md.trello.dtos.workspace.WorkspaceUserDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.mappers.WorkspaceUserMapper;
import uz.md.trello.repository.workspace.WorkspaceUserRepository;
import uz.md.trello.services.auth.InvitationService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.*;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 27/08/22 18:36
 */

@Service
public class WorkspaceUserService {
    private final WorkspaceUserRepository repository;
    private final WorkspaceUserMapper mapper;
    private final BaseUtils baseUtils;
    private final Privacy privacy;

    private final InvitationService invitationService;
    private final WorkspaceService workspaceService;

    public WorkspaceUserService(WorkspaceUserRepository repository,
                                WorkspaceUserMapper mapper,
                                BaseUtils baseUtils,
                                Privacy privacy,
                                InvitationService invitationService,
                                @Lazy WorkspaceService workspaceService){
        this.repository = repository;
        this.mapper = mapper;
        this.baseUtils = baseUtils;
        this.privacy = privacy;
        this.invitationService = invitationService;
        this.workspaceService = workspaceService;
    }

    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public List<WorkspaceUserDTO> getMembers(UUID id) {
        Workspace workspace = workspaceService.findEntityById(id);
        privacy.checkForMemberPermissionIfFoundGet(workspace,
                "Permission denied. Workspace is private");
        return mapper.toDtos(workspace.getMembers().stream().toList());
    }

    public WorkspaceUser getWorkspaceUserByUserId(UUID id, UUID workspaceId) {
        return repository.findUserByUserIDAndWorkspaceId(id,workspaceId).orElseThrow(() -> new GenericNotFoundException("Workspace User not found", 201));
    }


    private WorkspaceUser findWorkspaceUserById(UUID id) {
        return repository.findNotDeletedById(id).orElseThrow(() ->
                new GenericNotFoundException("Workspace User not found", 404));
    }

    public WorkspaceUser findMemberByIdAndRole(UUID id, Role role) {
        return repository.findMemberByIdAndRole(id, role).orElse(null);
    }

    public WorkspaceUser save(WorkspaceUser workspaceUser) {
        return repository.save(workspaceUser);
    }

    public WorkspaceUser getUserIfMemberOf(Workspace workspace) {
        String userName = baseUtils.getCurrentUserName();
        return workspace.getMembers().stream().filter(workspaceUser -> workspaceUser
                .getUser()
                .getUsername()
                .equals(userName))
                .findFirst()
                .orElseThrow(() -> new NotAllowedException("Permission denied. You are not member of this workspace", 405));
    }

    public WorkspaceUser getCurrentUser(Workspace workspace) {
        AuthUser currentUser = baseUtils.getCurrentUser();
        return getWorkspaceUserByUserId(currentUser.getId(), workspace.getId());
    }

    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    @Transactional
    public void inviteUser(InvitationRequest invitationRequest) {
        invitationService.send(invitationRequest);
    }

    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    @Transactional
    public void removeUser(RemoveUserRequest request) {
        Workspace workspace = workspaceService.findEntityById(request.workspaceId());
        WorkspaceUser workspaceUser = findWorkspaceUserById(request.userId());
        if (workspaceService.removeThisMember(workspace, workspaceUser)){
            repository.deleteById(workspaceUser.getId());
        }
    }

    public void changeUserRole(ChangeUserRoleRequest request) {
        Workspace workspace = workspaceService.findEntityById(request.workspaceId());
        WorkspaceUser changing = findWorkspaceUserById(request.changing());
        WorkspaceUser changedBy = getCurrentUser(workspace);
        privacy.checkForAdmin(workspace, changedBy);
        if (changing.getId().equals(changedBy.getId())){
            checkForAdminExistedExceptFromThis(workspace.getId(), changing.getId());
        }
        privacy.checkForMember(workspace, changing);
        repository.changeUserRole(changing.getId(), Role.getRole(request.role()));
    }

    private void checkForAdminExistedExceptFromThis(UUID workspaceId, UUID changingId) {
        repository.findUserExceptThisWhichIsAdmin(workspaceId, changingId).orElseThrow(
                ()-> new NotAllowedException("No other admin You can not change role", 405));
    }
}
