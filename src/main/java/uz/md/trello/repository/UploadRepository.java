package uz.md.trello.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.trello.domains.upload.Upload;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 17:27
 */
public interface UploadRepository extends JpaRepository<Upload, UUID> {
}
