package uz.md.trello.controller.board.list;

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
import uz.md.trello.dtos.board.list.*;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.board.list.BoardListService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sat 27/08/22 23:20
 */

@RestController
@Tag(name = "Board List Controller", description = "All Board List operations will be done by this controller")
public class BoardListController extends ApiController<BoardListService> {

    protected BoardListController(BoardListService service) {
        super(service);
    }

    @GetMapping(PATH + "/list")
    public ApiResponse<List<BoardListDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }

    @GetMapping(PATH + "/b/{id}/list")
    public ApiResponse<List<BoardListDTO>> getAllOfBoard(@PathVariable("id") UUID boardId) {
        return new ApiResponse<>(service.getAllOrderedByPositionOf(boardId));
    }



    @GetMapping(PATH + "/list/{id}")
    public ApiResponse<BoardListDTO> get(@PathVariable UUID id) {
        return new ApiResponse<>(service.findById(id));
    }

    @PostMapping(value = PATH + "/list", produces = {"application/json", "application/xml", "multipart/form-data"}, consumes = {"application/json", "application/xml", "multipart/form-data"})
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
    public ApiResponse<BoardListDTO> create(@Valid @RequestBody BoardListCDTO dto,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        BoardListDTO boardDTO = service.create(dto, userDetails.authUser().getId());
        return new ApiResponse<>(boardDTO, HttpStatus.OK, 1);
    }

    @DeleteMapping(PATH + "/list/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.softDeleteById(id);
        return new ApiResponse<>(200);
    }

    @DeleteMapping(PATH + "/list/{id}/archive")
    public ApiResponse<Void> archive(@PathVariable UUID id) {
        service.archiveById(id);
        return new ApiResponse<>(200);
    }

    @PatchMapping(PATH + "/list/{id}")
    public ApiResponse<BoardListDTO> update(@Valid @RequestBody BoardListUDTO boardUDTO, @AuthenticationPrincipal UserDetails userDetails) {
        BoardListDTO update = service.update(boardUDTO);
        return new ApiResponse<>(update, HttpStatus.OK, 1);
    }

    @PostMapping(PATH+"/list/copy")
    public ApiResponse<BoardListDTO> copy(@RequestBody BoardListCopyDTO copyDTO){
        BoardListDTO boardListDTO = service.copyThis(copyDTO);
        return new ApiResponse<>(boardListDTO);
    }

    @PostMapping(PATH+"/list/move")
    public ApiResponse<Void> move(@RequestBody BoardListMoveDTO moveDTO){
        service.moveThis(moveDTO);
        return new ApiResponse<>(200);
    }

}
