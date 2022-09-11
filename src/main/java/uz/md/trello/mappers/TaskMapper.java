package uz.md.trello.mappers;

import org.apache.catalina.LifecycleState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import uz.md.trello.domains.list.Task;
import uz.md.trello.dtos.task.TaskSimpDTO;
import uz.md.trello.dtos.task.TaskCDTO;
import uz.md.trello.dtos.task.TaskDTO;
import uz.md.trello.dtos.task.TaskUDTO;

import java.util.List;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Sun 28/08/22 12:03
 */

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface TaskMapper extends uz.md.trello.mappers.Mapper , EntityMapper<
        TaskDTO,
        TaskCDTO,
        TaskUDTO,
        Task
        >{

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mapping(target = "commentsCount", expression = "java(task.getComments().size())")
    TaskSimpDTO toSimpDTO(Task task);

    List<TaskSimpDTO> toSimpDTOs(List<Task> tasks);


}
