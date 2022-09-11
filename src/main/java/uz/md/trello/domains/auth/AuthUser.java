package uz.md.trello.domains.auth;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import uz.md.trello.domains.Auditable;
import uz.md.trello.domains.Domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser implements Domain {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String firstName;

    private String lastName;

    @OneToOne
    private Address address;

    private String phone;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(columnDefinition = "bool default false")
    private boolean deleted = false;

    @Column(unique = true, nullable = false)
    private String email;

    private LocalDateTime lastLoginTime;

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.NOT_ACTIVE;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "auth_user_auth_role",
            joinColumns = @JoinColumn(name = "auth_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "auth_role_id", referencedColumnName = "id")
    )
    private Collection<AuthRole> roles;





    public void setStatus(String status) {

        switch (status) {
            case "NOT_ACTIVE":
                this.status = Status.NOT_ACTIVE;
                break;
            case "BLOCKED":
                this.status = Status.BLOCKED;
                break;
            default:
                this.status = Status.ACTIVE;
        }
    }

    public enum Status {
        ACTIVE, NOT_ACTIVE, BLOCKED;
    }

    public boolean isActive() {
        return Status.ACTIVE.equals(this.status);
    }
}