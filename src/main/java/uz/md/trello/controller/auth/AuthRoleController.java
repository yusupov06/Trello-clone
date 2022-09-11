package uz.md.trello.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.controller.ApiController;
import uz.md.trello.dtos.auth.AuthRoleCDTO;
import uz.md.trello.dtos.auth.AuthRoleDTO;
import uz.md.trello.response.ApiErrorResponse;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.auth.AuthRoleService;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
public class AuthRoleController extends ApiController<AuthRoleService> {

    protected AuthRoleController(AuthRoleService service) {
        super(service);
    }

    @GetMapping(PATH + "/role")
    public ApiResponse<List<AuthRoleDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }

    @GetMapping(PATH + "/role/{id}")
    public ApiResponse<AuthRoleDTO> get(@PathVariable Long id) {
        return new ApiResponse<>(service.get(id));
    }

    @PostMapping(PATH + "/role")
    public ApiResponse<Void> create(@Valid @RequestBody AuthRoleCDTO dto) {
        AuthRoleDTO authRoleDTO = service.create(dto);
        if (Objects.nonNull(authRoleDTO)){
            return new ApiResponse<>(200);
        }
        // TODO: 19/08/22 standardize status codes
        return new ApiResponse<>(new ApiErrorResponse(
                "Role not created",
                "Role not created",
                Timestamp.valueOf(LocalDateTime.now()),
                PATH+"/role"
        ), HttpStatus.valueOf(201));
    }

    @DeleteMapping(PATH + "/role/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        service.delete(id);
        // TODO: 19/08/22 standardize status codes
        return new ApiResponse<>(204);
    }


}
