package uz.md.trello.controller.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.controller.ApiController;
import uz.md.trello.dtos.AddLabelRequest;
import uz.md.trello.dtos.task.*;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.task.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 12:01
 */
@RestController
@Tag(name = "Task Controller", description = "All Task operations will be done by this controller")
public class TaskController extends ApiController<TaskService> {

    protected TaskController(TaskService service) {
        super(service);
    }

    @GetMapping(PATH + "/task")
    public ApiResponse<List<TaskDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }


    @GetMapping(PATH + "/task/{id}")
    public ApiResponse<TaskDTO> get(@PathVariable UUID id) {
        return new ApiResponse<>(service.findById(id), HttpStatus.OK, 1);
    }

    @GetMapping(PATH + "/task/{id}/join")
    public ApiResponse<Void> join(@PathVariable UUID id) {
        service.join(id);
        return new ApiResponse<>(200);
    }

    @PostMapping(PATH + "/task")
    @RouterOperation(
            operation = @Operation(operationId = "create a workspace",
                    summary = "This api for creating workspace",
                    tags = {
                            "Create a workspace"
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "dto", description = "TaskCDTO")
                    },
                    responses = {
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Path not found")
                    }))
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<TaskDTO> create(@Valid @RequestBody TaskCDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        TaskDTO workspaceDTO = service.create(dto, userDetails.authUser().getId());
        return new ApiResponse<>(workspaceDTO, HttpStatus.OK, 1);
    }

    @PatchMapping(PATH + "/task/{id}")
    public ApiResponse<TaskDTO> update(@Valid @RequestBody TaskUDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        TaskDTO workspaceDTO = service.update(dto);
        return new ApiResponse<>(workspaceDTO, HttpStatus.OK, 1);
    }


    @DeleteMapping(PATH + "/task/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.softDeleteById(id);
        return new ApiResponse<>(200);
    }

    @DeleteMapping(PATH + "/task/{id}/archive")
    public ApiResponse<Void> archive(@PathVariable UUID id) {
        service.archiveById(id);
        return new ApiResponse<>(200);
    }

    @PostMapping(PATH+"/task/move")
    public ApiResponse<Void> move(@Valid @RequestBody TaskMoveDTO moveDTO){
        service.move(moveDTO);
        return new ApiResponse<>(200);
    }

    @PostMapping(PATH+"/task/copy")
    public ApiResponse<Void> copy(@Valid @RequestBody TaskCopyDTO copyDTO){
        service.copy(copyDTO);
        return new ApiResponse<>(200);
    }

    @PostMapping(PATH+"/task/add/label")
    public ApiResponse<Void> addLabel(@Valid @RequestBody AddLabelRequest request){
        service.addLabel(request);
        return new ApiResponse<>(200);
    }
}
