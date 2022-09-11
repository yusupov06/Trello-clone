package uz.md.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.md.trello.domains.workspace.Activity;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 17:31
 */
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
