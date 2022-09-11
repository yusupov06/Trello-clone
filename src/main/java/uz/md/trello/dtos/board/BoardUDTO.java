package uz.md.trello.dtos.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.md.trello.domains.board.BoardVisibility;
import uz.md.trello.domains.upload.Upload;
import uz.md.trello.dtos.DTO;
import uz.md.trello.dtos.board.list.BoardListDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:12
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class BoardUDTO implements DTO {


    private UUID id;

    private String boardTitle;

    private String description;

//    private Upload cover;

    private BoardVisibility visibility;

}
