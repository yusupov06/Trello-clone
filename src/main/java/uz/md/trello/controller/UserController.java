package uz.md.trello.controller;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.md.trello.config.security.UserDetails;
import uz.md.trello.domains.workspace.Activity;
import uz.md.trello.response.ApiResponse;
import uz.md.trello.services.auth.AuthUserService;

import java.util.List;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/11:03 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */
@RestController
public class UserController extends ApiController<AuthUserService> {

    protected UserController(@Lazy AuthUserService service) {
        super(service);
    }

    @GetMapping(value = PATH+"/profile/activity")
    public ApiResponse<List<Activity>> getAllActivities(@AuthenticationPrincipal UserDetails userDetails){



       return null;
    }

}
