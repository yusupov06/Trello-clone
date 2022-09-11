package uz.md.trello.services.auth;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.md.trello.domains.auth.ActivationCode;
import uz.md.trello.dtos.auth.AuthUserDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.repository.auth.ActivationCodeRepository;
import uz.md.trello.utils.BaseUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/11:22 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */

@Service

public class ActivationCodeService {
    private final BaseUtils baseUtils;
    private final ActivationCodeRepository repository;

    @Value("${activation.link.expiry.in.minutes}")
    private long activationLinkValidTillInMinutes;

    public ActivationCodeService(BaseUtils baseUtils,
                                 ActivationCodeRepository repository) {
        this.baseUtils = baseUtils;
        this.repository = repository;
    }

    public ActivationCode generateCode(@NonNull AuthUserDTO authUserDTO) {
        String codeForEncoding = "" + UUID.randomUUID() + System.currentTimeMillis();
        String encodedActivationCode = baseUtils.encode(codeForEncoding);
        ActivationCode activationCode = ActivationCode.builder()
                .activationCode(encodedActivationCode)
                .userId(authUserDTO.getId())
                .validTill(LocalDateTime.now().plusMinutes(activationLinkValidTillInMinutes))
                .build();
        return repository.save(activationCode);
    }

    public ActivationCode findByActivationCode(@NonNull String activationCode) {
        return repository.findByActivationCode(activationCode).orElseThrow(() ->
        {
            throw new GenericNotFoundException("Activation Link Not Found", 404);
        });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
