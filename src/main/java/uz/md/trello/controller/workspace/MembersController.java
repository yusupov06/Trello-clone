package uz.md.trello.controller.workspace;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.controller.ApiController;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.*;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.auth.InvitationService;
import uz.md.trello.services.workspace.WorkspaceService;
import uz.md.trello.services.workspace.WorkspaceUserService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 19:06
 */

@RestController
@Tag(name = "Workspace Members Controller", description = "All workspaces' members operations will be done by this controller")
public class MembersController extends ApiController<WorkspaceUserService> {

    protected MembersController(WorkspaceUserService service, InvitationService invitationService) {
        super(service);
        this.invitationService = invitationService;
    }

    private final InvitationService invitationService;

    @GetMapping(PATH + "/workspace/{id}/members")
    public ApiResponse<List<WorkspaceUserDTO>> getMembers(@PathVariable UUID id) {
        List<WorkspaceUserDTO> members = service.getMembers(id);
        return new ApiResponse<>(
                members,
                HttpStatus.OK,
                members.size()
        );
    }

    @GetMapping(PATH + "/workspace/members/accept_joining")
    public ApiResponse<WorkspaceDTO> acceptJoining(@RequestParam(name = "invitation") UUID id) {
        WorkspaceDTO workspaceDTO = invitationService.accept(id);
        return new ApiResponse<>(workspaceDTO, HttpStatus.OK, 1);
    }

    @PostMapping(PATH + "/workspace/members/invite")
    public ApiResponse<String> inviteUser(@Valid @RequestBody InvitationRequest invitationRequest) {
        service.inviteUser(invitationRequest);
        return new ApiResponse<>("Invitation sent", 1);
    }

    @PostMapping(PATH + "/workspace/members/remove")
    public ApiResponse<String> removeUser(@Valid @RequestBody RemoveUserRequest request) {
        service.removeUser(request);
        return new ApiResponse<>("User removed", 1);
    }

    @PostMapping(PATH + "/workspace/members/change/role")
    public ApiResponse<String> changeUserRole(@Valid @RequestBody ChangeUserRoleRequest request) {
        service.changeUserRole(request);
        return new ApiResponse<>("User Role Changed", 1);
    }

}
