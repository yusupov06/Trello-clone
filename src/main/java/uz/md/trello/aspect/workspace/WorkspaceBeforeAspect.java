package uz.md.trello.aspect.workspace;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.repository.workspace.WorkspaceUserRepository;

import java.security.Principal;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 18:10
 */

@Aspect
@RequiredArgsConstructor
public class WorkspaceBeforeAspect {

    private final WorkspaceUserRepository workspaceUserRepository;

    @Pointcut("@execution(uz.md.trello.services.workspace.WorkspaceService)")
    public void workspaceServiceMethods() {};


    @Before("workspaceServiceMethods()")
    public void checkForPermissionAdvice(){
        System.out.println(" ===================================== Checking for permission ====================================== ");
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        workspaceUserRepository
                .findMemberByUsername(principal.getName())
                .orElseThrow(() -> new NotAllowedException(" This workspace is private allowed only members", 405));
    }

//    @Before("execution(* com.journaldev.spring.service.*.get*())")
//    public void getAllAdvice(){
//        System.out.println("Service method getter called");
//    }
}
