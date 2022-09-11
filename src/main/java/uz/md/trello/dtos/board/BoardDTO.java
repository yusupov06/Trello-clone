package uz.md.trello.dtos.board;

import lombok.*;
import uz.md.trello.domains.board.BoardVisibility;
import uz.md.trello.domains.upload.Upload;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.board.list.BoardListDTO;
import uz.md.trello.dtos.board.user.BoardUserDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:11
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BoardDTO implements DTO {

    private UUID id;
    private String boardTitle;
    private String description;
    private List<BoardListDTO> lists = new ArrayList<>();
    private Upload cover;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BoardVisibility visibility;
    private List<BoardUserDTO> members;

}
