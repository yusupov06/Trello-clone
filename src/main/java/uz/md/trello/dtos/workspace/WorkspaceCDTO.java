package uz.md.trello.dtos.workspace;

import lombok.*;
import uz.md.trello.domains.workspace.WorkspaceType;
import uz.md.trello.dtos.DTO;

import javax.validation.constraints.NotBlank;


/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 19:03
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class WorkspaceCDTO implements DTO {

    @NotBlank(message = " Name can not be blank")
    private String name;

    private String description;

    private WorkspaceType type;

}
