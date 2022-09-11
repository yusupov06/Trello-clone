package uz.md.trello.dtos.workspace;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 05/09/22 16:40
 */
public record RemoveUserRequest(UUID workspaceId, UUID userId){
}
