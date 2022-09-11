package uz.md.trello.domains;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Me: muhammadqodir
 * Project: trello_with_b16/IntelliJ IDEA
 * Date:Wed 07/09/22 19:28
 */

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class A {

    @Id
    private Long id;

    private String name;

}

