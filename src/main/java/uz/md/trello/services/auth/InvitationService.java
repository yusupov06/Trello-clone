package uz.md.trello.services.auth;

import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.workspace.Role;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.domains.workspace.WorkspaceUser;
import uz.md.trello.dtos.workspace.Invitation;
import uz.md.trello.dtos.workspace.InvitationRequest;
import uz.md.trello.dtos.workspace.WorkspaceDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.exceptions.NotAllowedException;
import uz.md.trello.mappers.WorkspaceMapper;
import uz.md.trello.repository.InvitationRepository;
import uz.md.trello.services.mail.MailService;
import uz.md.trello.services.workspace.WorkspaceService;
import uz.md.trello.utils.Privacy;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Tue 30/08/22 12:34
 */

@Service
public class InvitationService {

    private final InvitationRepository repository;

    private final MailService mailService;
    private final WorkspaceService workspaceService;
    private final AuthUserService authUserService;
    private final WorkspaceMapper workspaceMapper;
    private final Privacy privacy;
    @Value("${invitation.link.base.path}")
    private String basePath;

    public InvitationService(InvitationRepository repository,
                             @Lazy MailService mailService,
                             @Lazy WorkspaceService workspaceService,
                             @Lazy AuthUserService authUserService, WorkspaceMapper workspaceMapper, Privacy privacy) {
        this.repository = repository;
        this.mailService = mailService;
        this.workspaceService = workspaceService;
        this.authUserService = authUserService;
        this.workspaceMapper = workspaceMapper;
        this.privacy = privacy;
    }

    @Async
    public void send(InvitationRequest invitationRequest) {
        Workspace workspace = workspaceService.findEntityById(invitationRequest.getWorkspaceId());
        AuthUser receiver = authUserService.findEntityById(invitationRequest.getReceiverId());
        AuthUser sender = workspace.getOwner().getUser();
        String invitationText = invitationRequest.getInvitationText();

        Invitation invitation = Invitation.builder()
                .sender(sender)
                .receiver(receiver)
                .invitationText(invitationText)
                .workspace(workspace)
                .build();
        Invitation save = repository.save(invitation);
        String link = basePath.formatted(save.getId());
        try {
            mailService.sendInvitation(save, link);
        } catch (MessagingException | TemplateException | IOException e) {
            throw new GenericNotFoundException(e.getMessage(), HttpStatus.BAD_REQUEST.value());
        }
    }

    public WorkspaceDTO accept(UUID id) {

        Invitation invitation = repository.findById(id).orElseThrow(() -> new NotAllowedException(" Bad invitation link", 405));
        privacy.checkForIsAuthenticatedUser(invitation);
        Workspace workspace = invitation.getWorkspace();
        WorkspaceUser normal = workspaceService.setWorkspaceMember(invitation.getReceiver(), Role.NORMAL, workspace);
        return workspaceMapper.toDTO(workspaceService.addMemberToWorkspace(workspace, normal));
    }
}
