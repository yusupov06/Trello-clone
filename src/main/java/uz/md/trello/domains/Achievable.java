package uz.md.trello.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 16:07
 */

public class Achievable extends Auditable{

    public Achievable(UUID id, boolean deleted, LocalDateTime createdAt,
                      LocalDateTime updatedAt, LocalDateTime deletedAt,
                      boolean archived) {
        super(id, deleted, createdAt, updatedAt, deletedAt);
        this.archived = archived;
    }

    public Achievable() {
    }

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean archived = false;

}
