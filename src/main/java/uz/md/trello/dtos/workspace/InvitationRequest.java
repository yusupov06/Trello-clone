package uz.md.trello.dtos.workspace;

import lombok.*;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 19:50
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class InvitationRequest {

    UUID workspaceId;
    UUID receiverId;
    String invitationText;
}

