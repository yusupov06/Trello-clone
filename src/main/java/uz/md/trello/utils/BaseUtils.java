package uz.md.trello.utils;

import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.repository.auth.AuthUserRepository;
import uz.md.trello.services.board.list.BoardListService;
import uz.md.trello.services.task.TaskService;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author "Elmurodov Javohir"
 * @since 22/08/22/11:31 (Monday)
 * spring-boot-features/IntelliJ IDEA
 */

@Component
public class BaseUtils {
    public  final Charset UTF_8 = StandardCharsets.UTF_8;
    public  final String OUTPUT_FORMAT = "%-20s:%s";

    public static Map<UUID, Integer> positionForTasks = new HashMap<>();
    public static Map<UUID, Integer> boardPositionForLists = new HashMap<>();

    private final BoardListService boardListService;
    private final TaskService taskService;
    private final AuthUserRepository authUserRepository;

    public BaseUtils(@Lazy BoardListService boardListService,
                     @Lazy TaskService taskService,
                     AuthUserRepository authUserRepository) {
        this.boardListService = boardListService;
        this.taskService = taskService;
        this.authUserRepository = authUserRepository;
    }

    private byte[] digest(byte[] input) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }
        return md.digest(input);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x",
                    b));
        }
        return sb.toString();
    }

    public String encode(@NonNull String codeForEncoding) {
        byte[] digest = digest(codeForEncoding.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    public AuthUser getCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return authUserRepository.findByUsername(username).orElseThrow(() -> {
            throw new GenericNotFoundException("User not found!", 404);
        });
    }

     public String getCurrentUserName(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
       return username;
    }

    public Integer getPositionTask(UUID id) {
//        Integer position = BaseUtils.positionForTasks.get(id);
//        if (increase) BaseUtils.positionForTasks.put(id, position + 1); else BaseUtils.positionForTasks.put(id, position - 1);
//        return position;
        return taskService.getCountOfNotDeletedAndNOtArchived(id)+1;
    }

    public Integer getPositionList(UUID boardId) {
//        Integer position = BaseUtils.boardPositionForLists.get(id);
//        if (increase) BaseUtils.positionForTasks.put(id, position + 1); else BaseUtils.positionForTasks.put(id, position - 1);
//        return position;
        return boardListService.getCountOfNotDeletedAndNOtArchived(boardId);
    }


}