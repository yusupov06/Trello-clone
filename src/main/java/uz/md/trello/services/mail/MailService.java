package uz.md.trello.services.mail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.dtos.auth.AuthUserDTO;
import uz.md.trello.dtos.workspace.Invitation;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MailService {


    private final Configuration configuration;

    private final JavaMailSender javaMailSender;



    @Async
    public void sendActivation(AuthUserDTO user, String activationLink) throws MessagingException, TemplateException, IOException {
        String subject = "Activation link for Trello";
        String sendTo = user.getEmail();
        Map<String, Object> model = Map.of(
                "username", user.getUsername(),
                "activation_link", activationLink
        );
        sendEmail(subject, sendTo, model, "activation.ftlh");
    }

    @Async
    public void sendInvitation(Invitation invitation, String link) throws MessagingException, TemplateException, IOException {

        AuthUser receiver = invitation.getReceiver();
        AuthUser sender = invitation.getSender();
        String subject = sender.getFirstName() + sender.getLastName()  + " is inviting you to Trello Workspace";
        String sendTo = receiver.getEmail();
        Map<String, Object> model = Map.of(
                "receiverFullName", receiver.getFirstName() +" " +receiver.getLastName() ,
                "senderFullName", sender.getFirstName() + " " + sender.getLastName(),
                "invitation_text", invitation.getInvitationText(),
                "workspace", invitation.getWorkspace().getName(),
                "invitation_link", link
        );

        sendEmail(subject, sendTo, model, "invitation.ftlh");
    }

    @Async
    public void sendEmail(String subject, String receiverEmail, Map<String, Object> model, String templateName) throws MessagingException, TemplateException, IOException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setTo(receiverEmail);
        String emailContent = getEmailContent(model, templateName);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    private String getEmailContent(Map<String, Object> model, String templateName) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        Template template = configuration.getTemplate(templateName);
        template.process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}

