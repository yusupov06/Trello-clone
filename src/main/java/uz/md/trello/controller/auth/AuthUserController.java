package uz.md.trello.controller.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.controller.ApiController;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.JwtResponse;
import uz.md.trello.dtos.LoginRequest;
import uz.md.trello.dtos.RefreshTokenRequest;
import uz.md.trello.dtos.UserRegisterDTO;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.auth.AuthUserService;

import javax.validation.Valid;
import java.util.List;


@RestController
public class AuthUserController extends ApiController<AuthUserService> {
    protected AuthUserController(AuthUserService service) {
        super(service);
    }

    @PostMapping(value = PATH + "/auth/login", produces = "application/json")
    public ApiResponse<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        return new ApiResponse<>(service.login(loginRequest));
    }

    @GetMapping(value = PATH + "/auth/refresh", produces = "application/json")
    public ApiResponse<JwtResponse> login(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return new ApiResponse<>(service.refreshToken(refreshTokenRequest));
    }

    @PostMapping(PATH + "/auth/register")
    public ApiResponse<AuthUser> register(@Valid @RequestBody UserRegisterDTO dto) {
        return new ApiResponse<>(service.register(dto));
    }

    @GetMapping(PATH + "/auth/activate")
    public ApiResponse<Boolean> register(@RequestParam(name = "activation_code") String activationCode) {
        return new ApiResponse<>(service.activateUser(activationCode));
    }

    @GetMapping(PATH + "/auth/me")
    public List<? extends GrantedAuthority> me(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return userDetails.getAuthorities().stream().toList();
    }
}
