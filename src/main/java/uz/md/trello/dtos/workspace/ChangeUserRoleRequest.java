package uz.md.trello.dtos.workspace;

import uz.md.trello.domains.workspace.Role;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 05/09/22 22:01
 */
public record ChangeUserRoleRequest(UUID workspaceId, UUID changing, String role) {
}
