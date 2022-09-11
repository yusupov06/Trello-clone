package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.list.Task;
import uz.md.trello.dtos.board.list.BoardListDTO;
import uz.md.trello.dtos.board.list.BoardListCDTO;
import uz.md.trello.dtos.board.list.BoardListUDTO;
import uz.md.trello.dtos.task.TaskSimpDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Fri 26/08/22 10:44
 */
@Mapper(componentModel = "spring", uses = {TaskMapper.class})
public interface BoardListMapper extends EntityMapper<
        BoardListDTO,
        BoardListCDTO,
        BoardListUDTO,
        BoardList
        >, uz.md.trello.mappers.Mapper {

    @Override
    @Mapping(target = "tasks", expression = "java(getTasksNotDeletedNotArchived(entity))")
    BoardListDTO toDto(BoardList entity);

    default List<TaskSimpDTO> getTasksNotDeletedNotArchived(BoardList boardList) {
        if (Objects.isNull(boardList.getTasks())){
            return new ArrayList<>();
        }
        return boardList.getTasks().stream()
                .filter(task -> (!task.isDeleted() && !task.isArchived()))
                .map(TaskMapper.INSTANCE::toSimpDTO).toList();
    }
}
