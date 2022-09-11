package uz.md.trello.domains.workspace;

import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import uz.md.trello.exceptions.GenericNotFoundException;

import javax.print.DocFlavor;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Wed 24/08/22 15:55
 */
public enum Role {
    ADMIN, NORMAL;

    public static Role getRole(@NonNull String role){
        return switch (role.toUpperCase()){
            case "ADMIN" -> ADMIN;
            case "NORMAL" -> NORMAL;
            default -> throw new GenericNotFoundException("Role not found",404);
        };
    }

}
