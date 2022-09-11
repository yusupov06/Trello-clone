package uz.md.trello.services.auth;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.domains.auth.ActivationCode;
import uz.md.trello.domains.auth.AuthRole;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.JwtResponse;
import uz.md.trello.dtos.LoginRequest;
import uz.md.trello.dtos.RefreshTokenRequest;
import uz.md.trello.dtos.UserRegisterDTO;
import uz.md.trello.dtos.auth.AuthUserDTO;
import uz.md.trello.dtos.auth.AuthUserUDTO;
import uz.md.trello.exceptions.GenericInvalidTokenException;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.exceptions.GenericRuntimeException;
import uz.md.trello.exceptions.NotDeletedException;
import uz.md.trello.mappers.AuthUserMapper;
import uz.md.trello.repository.auth.AuthUserRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.mail.MailService;
import uz.md.trello.utils.jwt.TokenService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/12:07 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */

@Service
@Slf4j
public class AuthUserService extends AbstractService<
        AuthUser,
        AuthUserDTO,
        UserRegisterDTO,
        AuthUserUDTO,
        UUID,
        AuthUserMapper,
        AuthUserRepository
        > implements UserDetailsService {
    private final AuthenticationManager authenticationManager;
    private final TokenService accessTokenService;
    private final TokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ActivationCodeService activationCodeService;
    private final AuthRoleService authRoleService;
    @Value("${activation.link.base.path}")
    private String basePath;

    public AuthUserService(@Lazy AuthenticationManager authenticationManager,
                           @Lazy AuthUserRepository authUserRepository,
                           @Qualifier("accessTokenService") TokenService accessTokenService,
                           @Qualifier("refreshTokenService") TokenService refreshTokenService,
                           AuthUserMapper authUserMapper,
                           PasswordEncoder passwordEncoder,
                           MailService mailService,
                            ActivationCodeService activationCodeService,
                           AuthRoleService authRoleService) {
        super(authUserRepository, authUserMapper);
        this.authenticationManager = authenticationManager;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.activationCodeService = activationCodeService;
        this.authRoleService = authRoleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User loading by username %s".formatted(username));
        Supplier<UsernameNotFoundException> exception = () ->
                new UsernameNotFoundException("Bad credentials");
        AuthUser authUser = repository.findByUsername(username).orElseThrow(exception);
        return new UserDetails(authUser);
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = accessTokenService.generateToken(userDetails);
        String refreshToken = refreshTokenService.generateToken(userDetails);
        AuthUser authUser = userDetails.authUser();
        authUser.setLastLoginTime(LocalDateTime.now());
        repository.save(authUser);
        return new JwtResponse(accessToken, refreshToken, "Bearer");
    }

    public JwtResponse refreshToken(@NonNull RefreshTokenRequest request) {
        String token = request.token();
        if (accessTokenService.isValid(token)) {
            throw new GenericInvalidTokenException("Refresh Token invalid", 401);
        }
        String subject = accessTokenService.getSubject(token);
        UserDetails userDetails = loadUserByUsername(subject);
        String accessToken = accessTokenService.generateToken(userDetails);
        return new JwtResponse(accessToken, request.token(), "Bearer");
    }

    @SneakyThrows
    @Transactional
    public AuthUser register(UserRegisterDTO dto) {
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        AuthUser authUser = mapper.fromRegisterDTO(dto);
        setDefaultRole(authUser);
        repository.save(authUser);
        AuthUserDTO authUserDTO = mapper.toDTO(authUser);
        ActivationCode activationCode = activationCodeService.generateCode(authUserDTO);
        String link = basePath.formatted(activationCode.getActivationCode());
        mailService.sendActivation(authUserDTO, link);
        return authUser;
    }

    private void setDefaultRole(AuthUser authUser) {
        AuthRole role = authRoleService.findByName("User");
        Set<AuthRole> roles  = new HashSet<>();
        roles.add(role);
        authUser.setRoles(roles);
    }

    @Transactional(noRollbackFor = GenericRuntimeException.class)
    public Boolean activateUser(String activationCode) {
        ActivationCode activationLink = activationCodeService.findByActivationCode(activationCode);
        if (activationLink.getValidTill().isBefore(LocalDateTime.now())) {
            activationCodeService.delete(activationLink.getId());
            throw new GenericRuntimeException("Activation Code is not active", 400);
        }

        AuthUser authUser = repository.findById(activationLink.getUserId()).orElseThrow(() -> {
            throw new GenericNotFoundException("User not found", 404);
        });

        authUser.setStatus(AuthUser.Status.ACTIVE.name());
        repository.save(authUser);
        return true;
    }

    @Override
    public List<AuthUserDTO> getAll() {
        return mapper.toDTOs(repository.findAll());
    }

    @Override
    public AuthUserDTO findById(UUID id) {
        return mapper.toDTO(repository.findById(id).orElseThrow(() -> new GenericNotFoundException("User not found", 500)));
    }

    @Override
    public AuthUserDTO create(UserRegisterDTO userRegisterDTO, UUID createdBy) {
        AuthUser authUser = mapper.fromRegisterDTO(userRegisterDTO);
        AuthUser save = repository.save(authUser);
        return mapper.toDTO(save);
    }

    @Override
    public void deleteById(UUID id) {
        AuthUser deleted = repository.findById(id).orElseThrow(() -> new NotDeletedException("User not deleted", 201));
        repository.delete(deleted);
    }

    public AuthUser findEntityById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new GenericNotFoundException("User not found", 500));
    }

    @Override
    public void softDeleteById(UUID id) {
        findEntityById(id);
        repository.softDeleteById(id);
    }

    @Override
    public void softDelete(AuthUser entity) {
        softDeleteById(entity.getId());
    }

    @Override
    public AuthUserDTO update(AuthUserUDTO authUserUDTO) {
        return null;
    }
}
