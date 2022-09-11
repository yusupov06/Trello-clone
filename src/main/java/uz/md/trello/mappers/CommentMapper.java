package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import uz.md.trello.domains.auth.AuthUser;
import uz.md.trello.domains.list.Comment;
import uz.md.trello.domains.list.Task;
import uz.md.trello.dtos.task.comment.CommentDTO;
import uz.md.trello.dtos.task.comment.CommentCDTO;
import uz.md.trello.dtos.task.comment.CommentUDTO;
import uz.md.trello.services.task.TaskService;

import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 02/09/22 15:15
 */

@Mapper(componentModel = "spring", uses = {BoardUserMapper.class}, imports = {TaskService.class})
public interface CommentMapper extends uz.md.trello.mappers.Mapper, EntityMapper<
        CommentDTO,
        CommentCDTO,
        CommentUDTO,
        Comment
        > {

    @Override
    @Mapping(target = "fullName", expression = "java(getFullName(entity))")
    @Mapping(target = "ownerId", expression = "java(entity.getOwner().getId())")
    CommentDTO toDto(Comment entity);


    default String getFullName(Comment comment){
       AuthUser user = comment.getOwner().getUser();
       return user.getLastName()+" "+user.getFirstName();
   }
}
