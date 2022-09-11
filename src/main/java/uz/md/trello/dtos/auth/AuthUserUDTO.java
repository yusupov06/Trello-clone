package uz.md.trello.dtos.auth;

import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import uz.md.trello.dtos.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 12:01
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthUserUDTO implements DTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String username;
    private String email;
    private String city;
    private String state;
    private String zip;
}
