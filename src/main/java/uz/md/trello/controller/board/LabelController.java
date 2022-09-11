package uz.md.trello.controller.board;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.controller.ApiController;
import uz.md.trello.dtos.board.BoardCDTO;
import uz.md.trello.dtos.board.BoardDTO;
import uz.md.trello.dtos.label.LabelCDTO;
import uz.md.trello.dtos.label.LabelDTO;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.task.LabelService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 05/09/22 10:58
 */

@RestController
public class LabelController extends ApiController<LabelService> {


    protected LabelController(LabelService service) {
        super(service);
    }

    @GetMapping(PATH + "/b/{id}/all/labels")
    public ApiResponse<List<LabelDTO>> getAllOfBoard(@PathVariable("id") UUID boardId) {
        return new ApiResponse<>(service.getAllOf(boardId));
    }

    @PostMapping(PATH + "/label")
    public ApiResponse<LabelDTO> create(@Valid @RequestBody LabelCDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        LabelDTO labelDTO = service.create(dto, null);
        return new ApiResponse<>(labelDTO, HttpStatus.OK, 1);
    }

}
