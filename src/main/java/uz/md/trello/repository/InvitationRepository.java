package uz.md.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.trello.dtos.workspace.Invitation;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Tue 30/08/22 12:29
 */
public interface InvitationRepository extends JpaRepository<Invitation, UUID>{
}
