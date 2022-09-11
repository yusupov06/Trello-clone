package uz.md.trello.services.workspace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.*;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.WorkspaceMapper;
import uz.md.trello.repository.workspace.WorkspaceRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.auth.AuthUserService;
import uz.md.trello.services.mail.MailService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 19:00
 */

@Service
@Slf4j
@CacheConfig(cacheNames = {"workspace"})
public class WorkspaceService extends AbstractService<
        Workspace,
        WorkspaceDTO,
        WorkspaceCDTO,
        WorkspaceUDTO,
        UUID,
        WorkspaceMapper,
        WorkspaceRepository
        > {

    private final AuthUserService authUserService;
    private final MailService mailService;
    private final Privacy privacy;
    private final BaseUtils baseUtils;
    private final WorkspaceUserService workspaceUserService;

    public WorkspaceService(WorkspaceRepository repository,
                            WorkspaceMapper mapper,
                            AuthUserService authUserService,
                            @Lazy MailService mailService,
                            Privacy privacy,
                            BaseUtils baseUtils, WorkspaceUserService workspaceUserService) {
        super(repository, mapper);
        this.authUserService = authUserService;
        this.privacy = privacy;
        this.baseUtils = baseUtils;
        this.workspaceUserService = workspaceUserService;
        this.mailService = mailService;
    }

    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    @Override
    public List<WorkspaceDTO> getAll() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return mapper.toDTOs(repository.getAllNotDeletedOfUser(name));
    }

    @PreAuthorize(value = "isAuthenticated()" + " && hasAnyAuthority('WORKSPACE_READ')")
    @Override
    @Transactional
    @Cacheable(value = "workspace")
    public WorkspaceDTO findById(UUID id) {
        log.info("Getting workspace by id");
        Workspace workspace = findEntityById(id);
        privacy.checkForMemberPermissionIfFoundGet(workspace, "Permission denied. Workspace is private");
        return mapper.toDTO(workspace);
    }

    @PreAuthorize(value = "isAuthenticated() && hasAnyAuthority('WORKSPACE_CREATE')")
    @Override
    @Transactional
    public WorkspaceDTO create(WorkspaceCDTO workspaceCDTO, UUID createdBy) {
        Workspace workspace = mapper.fromCDTO(workspaceCDTO);
        AuthUser user = authUserService.findEntityById(createdBy);
        WorkspaceUser owner = setWorkspaceMember(user, Role.ADMIN, workspace);
        Workspace workspace1 = addMemberToWorkspace(workspace, owner);
        workspace1.setOwner(owner);
        return mapper.toDTO(repository.save(workspace1));
    }

    public WorkspaceUser setWorkspaceMember(AuthUser user, Role role, Workspace workspace) {
        return workspaceUserService.save(WorkspaceUser.builder()
                .role(role)
                .user(user)
                .workspace(workspace)
                .build());
    }

    @PreAuthorize(value = "isAuthenticated()" + " && hasAnyAuthority('WORKSPACE_DELETE')")
    @Override
    @Transactional
    @CacheEvict(cacheNames = "workspace", key = "#id")
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }


    @PreAuthorize(value = "isAuthenticated()" + " && hasAnyAuthority('WORKSPACE_DELETE')")
    @Override
    @CacheEvict(cacheNames = "workspace", key = "#id")
    public void softDeleteById(UUID id) {
        Workspace workspace = findEntityById(id);
        System.out.println("=================== workspace ===================== " + workspace);
        privacy.checkForOwnerPermission(workspace);
        repository.softDeleteById(id);
    }

    @PreAuthorize(value = "isAuthenticated()" + " && hasAnyAuthority('WORKSPACE_DELETE')")
    @Override
    @Transactional
    public void softDelete(Workspace workspace) {
        softDeleteById(workspace.getId());
    }

    @PreAuthorize(value = "isAuthenticated()" + " && hasAnyAuthority('WORKSPACE_UPDATE')")
    @Override
    @Transactional
    @CachePut(value = "workspace", key = "#workspaceUDTO.id")
    public WorkspaceDTO update(WorkspaceUDTO workspaceUDTO) {
        Workspace workspace1 = findEntityById(workspaceUDTO.getId());
        privacy.checkForMemberPermissionIfFoundGet(workspace1, "Permission denied. Workspace is private");
        Workspace workspace = mapper.fromUDTO(workspaceUDTO, workspace1);
        Workspace save = repository.save(workspace);
        return mapper.toDTO(save);
    }

    @PreAuthorize(value = "isAuthenticated() && hasAuthority('WORKSPACE_READ')")
    public List<Board> getBoards(UUID id) {
        Workspace workspace = findEntityById(id);
        privacy.checkForMemberPermissionIfFoundGet(workspace, "Permission denied. Workspace is private");
        return workspace.getBoards().stream().toList();
    }


    @Cacheable(value = "workspace", key = "#id")
    public Workspace findEntityById(UUID id) {
        System.out.println("======================= entity ========================================");
        return repository.findNotDeletedById(id).orElseThrow(() -> new GenericNotFoundException("Workspace not found", 404));
    }

    public Workspace addMemberToWorkspace(Workspace workspace, WorkspaceUser workspaceUser) {
        Set<WorkspaceUser> members = workspace.getMembers();
        members.add(workspaceUser);
        workspace.setMembers(members);
        return workspace;
    }

    public boolean removeThisMember(Workspace workspace, WorkspaceUser workspaceUser) {
        privacy.checkForAdminPermission(workspace);
        Set<WorkspaceUser> members = workspace.getMembers();
        members.remove(workspaceUser);
        workspace.setMembers(members);
        repository.save(workspace);
        return true;
    }

    public List<WorkspaceDTO> getAllOfUser() {
        AuthUser currentUser = baseUtils.getCurrentUser();
        List<Workspace> workspaces = repository.findAllByUserId(currentUser.getId()).orElse(new ArrayList<>());
        return mapper.toDTOs(workspaces);
    }
}
