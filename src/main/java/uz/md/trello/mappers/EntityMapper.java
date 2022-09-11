package uz.md.trello.mappers;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 18:25
 */

public interface EntityMapper<
        D,
        CD,
        UD,
        E> {

    E fromDTO(D dto);

    E fromCDTO(CD cd);

    E fromUDTO(UD ud, @MappingTarget E entity);

    D toDto(E entity);

    List<E> fromDTOs(List<D> dtoList);

    List<D> toDtos(List<E> entityList);
}