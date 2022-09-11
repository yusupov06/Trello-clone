package uz.md.trello.services;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.md.trello.domains.Domain;
import uz.md.trello.dtos.DTO;
import uz.md.trello.mappers.Mapper;

import java.io.Serializable;
import java.util.List;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 25/08/22 08:43
 */

@AllArgsConstructor
public abstract class AbstractService<
        D extends Domain,
        Dto extends DTO,
        CDTO extends DTO,
        UDTO extends DTO,
        ID extends Serializable,
        M extends Mapper,
        R extends JpaRepository<D, ID>
        > {

    protected final R repository;
    protected final M mapper;

    public abstract List<Dto> getAll();

    public abstract Dto findById(ID id);

    public abstract Dto create(CDTO cdto,ID createdBy);

    public abstract void deleteById(ID id) ;

    public abstract void softDeleteById(ID id) ;

    public abstract void softDelete(D entity);

    public abstract Dto update(UDTO udto);

    public abstract D findEntityById(ID id);


}
