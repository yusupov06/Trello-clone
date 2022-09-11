package uz.md.trello.services.auth;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import uz.md.trello.domains.auth.AuthRole;
import uz.md.trello.dtos.auth.AuthRoleCDTO;
import uz.md.trello.dtos.auth.AuthRoleDTO;
import uz.md.trello.dtos.auth.AuthRoleUDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.AuthRoleMapper;
import uz.md.trello.repository.auth.AuthRoleRepository;
import uz.md.trello.services.AbstractService;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author "Elmurodov Javohir"
 * @since 19/08/22/15:48 (Friday)
 * spring-boot-features/IntelliJ IDEA
 */

@Service
public class AuthRoleService extends AbstractService<
        AuthRole,
        AuthRoleDTO,
        AuthRoleCDTO,
        AuthRoleUDTO,
        Long,
        AuthRoleMapper,
        AuthRoleRepository
        > {


    public AuthRoleService(AuthRoleRepository repository, AuthRoleMapper mapper) {
        super(repository, mapper);
    }

//    @PreAuthorize("hasAuthority(T(uz.md.trello.enums.Permissions).ROLE_READ)")
    public List<AuthRoleDTO> getAll() {
        List<AuthRole> authRoles = repository.findAll();
        return mapper.toDTO(authRoles);
    }

    @Override
    public AuthRoleDTO findById(Long id) {
        AuthRole authRole = repository.findById(id).orElseThrow(() -> new GenericNotFoundException(" Role not found", 201));
        return mapper.toDTO(authRole);
    }

    @Override
    public AuthRoleDTO create(AuthRoleCDTO authRoleCDTO, Long createdBy) {
        AuthRole authRole = mapper.fromCreateDTO(authRoleCDTO);
        AuthRole save = repository.save(authRole);
        return mapper.toDTO(save);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }


    @Override
    public void softDeleteById(Long id) {
        repository.softDeleteById(id);
    }

    @Override
    public void softDelete(AuthRole entity) {
        softDeleteById(entity.getId());
    }

    @Override
    public AuthRoleDTO update(AuthRoleUDTO authRoleUDTO) {
        AuthRole authRole = mapper.fromUDTO(authRoleUDTO);
        AuthRole save = repository.save(authRole);
        return mapper.toDTO(save);
    }

    @Override
    public AuthRole findEntityById(Long aLong) {
        return null;
    }

    //    @PreAuthorize("hasAuthority(T(uz.md.trello.enums.Permissions).ROLE_READ)")
    public AuthRoleDTO get(@NonNull Long id) {
        // TODO: 19/08/22 standardize status codes
        Supplier<GenericNotFoundException> notFoundException = () -> new GenericNotFoundException("Role not found", 404);
        AuthRole authRole = repository.findById(id).orElseThrow(notFoundException);
        return mapper.toDTO(authRole);
    }

//    @PreAuthorize("hasAuthority(T(uz.md.trello.enums.Permissions).ROLE_CREATE)")
    public AuthRoleDTO create(AuthRoleCDTO dto) {
        AuthRole authRole = mapper.fromCreateDTO(dto);
        repository.save(authRole);
        return mapper.toDTO(authRole);
    }

//    @PreAuthorize("hasAuthority(T(uz.md.trello.enums.Permissions).ROLE_DELETE)")
    public void delete(@NonNull Long id) {
        repository.deleteById(id);

    }

    public AuthRole findByName(String name) {
        return repository.findByName(name).orElseThrow(()->new GenericNotFoundException("Role not found",201));
    }
}
