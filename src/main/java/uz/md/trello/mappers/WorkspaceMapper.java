package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uz.md.trello.domains.workspace.Workspace;
import uz.md.trello.dtos.workspace.WorkspaceCDTO;
import uz.md.trello.dtos.workspace.WorkspaceDTO;
import uz.md.trello.dtos.workspace.WorkspaceUDTO;

import java.util.List;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 09:00
 */

@Mapper(componentModel = "spring", uses = { BoardMapper.class, WorkspaceUserMapper.class })
public interface WorkspaceMapper extends uz.md.trello.mappers.Mapper {

    @Mapping( target = "owner", expression = "java(new uz.md.trello.domains.workspace.WorkspaceUser())")
    Workspace fromDTO(WorkspaceDTO dto);
    @Mapping(target = "owner", expression = "java(new uz.md.trello.domains.workspace.WorkspaceUser())")
    Workspace fromCDTO(WorkspaceCDTO dto);
    Workspace fromUDTO(WorkspaceUDTO dto, @MappingTarget Workspace workspace);
    WorkspaceDTO toDTO(Workspace entity);
    List<Workspace> fromDTOs(List<WorkspaceDTO> dtos);
    List<WorkspaceDTO> toDTOs(List<Workspace> entities);

}
