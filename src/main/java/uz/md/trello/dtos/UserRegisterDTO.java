package uz.md.trello.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:23 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterDTO implements DTO {

    private String firstName;

    private String lastName;

    private String phone;

    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "mail should be like mygmail@gmail.com")
    private String email;
}
