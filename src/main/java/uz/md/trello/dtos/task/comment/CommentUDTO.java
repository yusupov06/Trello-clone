package uz.md.trello.dtos.task.comment;

import uz.md.trello.dtos.DTO;

import java.util.UUID;


/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 15:15
 */
public record CommentUDTO(UUID commentId, String repliedUsername, String body) implements DTO {
}
