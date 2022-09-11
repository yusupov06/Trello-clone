package uz.md.trello.mappers;

import org.mapstruct.Mapper;
import uz.md.trello.domains.list.Label;
import uz.md.trello.dtos.label.LabelCDTO;
import uz.md.trello.dtos.label.LabelDTO;
import uz.md.trello.dtos.label.LabelUDTO;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 15:49
 */

@Mapper(componentModel = "spring")
public interface LabelMapper extends EntityMapper<
        LabelDTO,
        LabelCDTO,
        LabelUDTO,
        Label
        >, uz.md.trello.mappers.Mapper {
}
