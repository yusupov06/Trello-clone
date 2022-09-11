package uz.md.trello.dtos;

import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 04/09/22 15:31
 */

public record AddLabelRequest(UUID taskId, Long labelId) {
}
