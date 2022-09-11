package uz.md.trello.dtos.board;

import lombok.*;
import uz.md.trello.domains.board.BoardUser;
import uz.md.trello.domains.board.BoardVisibility;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.upload.Upload;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.dtos.DTO;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
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

public class BoardCDTO implements DTO {

    @NotBlank(message = "Board title can not be blank")
    private String boardTitle;

    private String description;

    @NonNull
    @NotBlank(message = "Workspace can not be blank")
    private UUID workspaceId;

    //    private Upload cover;

    @NotBlank(message = "Board visibility can not be blank")
    private BoardVisibility visibility;

}
