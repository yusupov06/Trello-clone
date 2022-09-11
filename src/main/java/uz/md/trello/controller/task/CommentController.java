package uz.md.trello.controller.task;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.controller.ApiController;
import uz.md.trello.dtos.task.comment.CommentDTO;
import uz.md.trello.dtos.task.comment.CommentCDTO;
import uz.md.trello.dtos.task.comment.CommentUDTO;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.task.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 16:03
 */

@RestController
public class CommentController extends ApiController<CommentService> {

    protected CommentController(CommentService service) {
        super(service);
    }

    @GetMapping(PATH + "/comment")
    public ApiResponse<List<CommentDTO>> getAll() {
        return new ApiResponse<>(service.getAll());
    }


    @GetMapping(PATH + "/comment/{id}")
    public ApiResponse<CommentDTO> get(@PathVariable UUID id) {
        return new ApiResponse<>(service.findById(id), HttpStatus.OK, 1);
    }

    @PostMapping(PATH + "/comment")
   @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CommentDTO> create(@Valid @RequestBody CommentCDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        CommentDTO commentDTO = service.create(dto, null);
        return new ApiResponse<>(commentDTO, HttpStatus.OK, 1);
    }

    @PatchMapping(PATH + "/comment/update")
    public ApiResponse<CommentDTO> update(@Valid @RequestBody CommentUDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        CommentDTO commentDTO = service.update(dto);
        return new ApiResponse<>(commentDTO, HttpStatus.OK, 1);
    }

    @DeleteMapping(PATH + "/comment/{id}/delete")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service.softDeleteById(id);
        return new ApiResponse<>(200);
    }

}
