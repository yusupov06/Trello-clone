package uz.md.trello.domains;//package uz.jl.trello.trello.domains;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public class B implements Serializable {

    @Id
    @MapsId
    @OneToOne
    @JoinColumn(name = "aId")
    private A a;

    private String title;
}
