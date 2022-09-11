package uz.md.trello.controller.workspace;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.controller.ApiController;
import uz.md.trello.dtos.workspace.InvitationRequest;
import uz.md.trello.dtos.workspace.WorkspaceCDTO;
import uz.md.trello.dtos.workspace.WorkspaceDTO;
import uz.md.trello.dtos.workspace.WorkspaceUDTO;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.workspace.WorkspaceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 19:00
 */

@RestController
@EnableCaching
@Tag(name = "Workspace Controller", description = "All workspace operations will be done by this controller")
public class WorkspaceController extends ApiController<WorkspaceService> {


    protected WorkspaceController(WorkspaceService service) {
        super(service);
    }

    @GetMapping(PATH + "/workspace")
    public ApiResponse<List<WorkspaceDTO>> getAllOfUser() {
        return new ApiResponse<>(service.getAllOfUser());
    }


    @GetMapping(PATH + "/workspace/{id}")
    public ApiResponse<WorkspaceDTO> get(@PathVariable UUID id) {
        return new ApiResponse<>(service.findById(id),HttpStatus.OK,1);
    }

    @PostMapping(PATH + "/workspace")
    @RouterOperation(
            operation = @Operation(operationId = "create a workspace",
                    summary = "This api for creating workspace",
                    tags = {
                            "Create a workspace"
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "dto", description = "WorkspaceCDTO")
                    },
                    responses = {
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Path not found")
                    }))
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<WorkspaceDTO> create(@Valid @RequestBody WorkspaceCDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        WorkspaceDTO workspaceDTO = service.create(dto, userDetails.authUser().getId());
        return new ApiResponse<>(workspaceDTO, HttpStatus.OK, 1);
    }

    @PatchMapping(PATH+"/workspace/{id}")
    public ApiResponse<WorkspaceDTO> update(@Valid @RequestBody WorkspaceUDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        WorkspaceDTO workspaceDTO = service.update(dto);
        return new ApiResponse<>(workspaceDTO, HttpStatus.OK, 1);
    }

    @DeleteMapping(PATH + "/workspace/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.softDeleteById(id);
        return new ApiResponse<>(200);
    }


}
