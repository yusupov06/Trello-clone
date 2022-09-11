package uz.md.trello.controller.board;

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
import uz.md.trello.dtos.board.BoardCDTO;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.dtos.board.BoardReorderRequest;
import uz.md.trello.dtos.board.BoardUDTO;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.board.BoardService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 08:48
 */

@RestController
@Tag(name = "Board Controller", description = "All Board operations will be done by this controller")
public class BoardController extends ApiController<BoardService> {

    protected BoardController(BoardService service) {
        super(service);
    }

    @GetMapping(PATH + "/board")
    public ApiResponse<List<BoardDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }

    @GetMapping(PATH + "/w/{id}/all")
    public ApiResponse<List<BoardDTO>> getAllOfWorkspace(@PathVariable("id") UUID workspaceId) {
        return new ApiResponse<>(service.getAllBoardsOfWorkspace(workspaceId));
    }

    @GetMapping(PATH + "/w/{id}/yourBoards")
    public ApiResponse<List<BoardDTO>> getAllOfYours(@PathVariable("id") UUID workspaceId) {
        return new ApiResponse<>(service.getAllBoardsOfWorkspaceByOwner(workspaceId));
    }



    @GetMapping(PATH + "/board/{id}")
    public ApiResponse<BoardDTO> get(@PathVariable UUID id) {
        return new ApiResponse<>(service.findById(id));
    }

    @PostMapping(PATH + "/board")
    @RouterOperation(
            operation = @Operation(operationId = "create a board",
                    summary = "This api for creating board",
                    tags = {
                            "Create a board"
                    },
                    parameters = {
                            @Parameter(in = ParameterIn.PATH, name = "dto", description = "BoardCDTO")
                    },
                    responses = {
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad request"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Created"),
                            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Path not found")
                    }))
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BoardDTO> create(@Valid @RequestBody BoardCDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        BoardDTO boardDTO = service.create(dto, userDetails.authUser().getId());
        return new ApiResponse<>(boardDTO, HttpStatus.OK, 1);
    }

    @DeleteMapping(PATH + "/board/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.softDeleteById(id);
        return new ApiResponse<>(200);
    }

    @DeleteMapping(PATH + "/board/archive/{id}")
    public ApiResponse<Void> archive(@PathVariable UUID id) {
        service.archiveById(id);
        return new ApiResponse<>(200);
    }

    @PatchMapping(PATH + "/board/{id}")
    public ApiResponse<BoardDTO> update(@Valid @RequestBody BoardUDTO boardUDTO, @AuthenticationPrincipal UserDetails userDetails) {
        BoardDTO update = service.update(boardUDTO);
        return new ApiResponse<>(update, HttpStatus.OK, 1);
    }



}
