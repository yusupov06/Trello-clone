package uz.md.trello.controller.board;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uz.md.trello.controller.ApiController;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.board.BoardUserService;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 04/09/22 16:24
 */
@RestController
public class BoardUserController extends ApiController<BoardUserService> {

    protected BoardUserController(BoardUserService service) {
        super(service);
    }

    @GetMapping(PATH + "/board/{id}/join")
    public ApiResponse<Void> join(@PathVariable UUID id) {
        service.joinToTheBoard(id);
        return new ApiResponse<>(HttpStatus.OK.value());
    }

}
