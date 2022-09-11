package uz.md.trello.domains.auth;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/11:19 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Future(message = "valid till must be future time")
    private LocalDateTime validTill;

    @Column(unique = true, nullable = false)
    private String activationCode;

    private boolean deleted;

    @Column(unique = true, nullable = false)
    private UUID userId;

}
