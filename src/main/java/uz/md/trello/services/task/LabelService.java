package uz.md.trello.services.task;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.list.Label;
import uz.md.trello.dtos.label.LabelCDTO;
import uz.md.trello.dtos.label.LabelDTO;
import uz.md.trello.dtos.label.LabelUDTO;
import uz.md.trello.exceptions.GenericNotFoundException;
import uz.md.trello.mappers.LabelMapper;
import uz.md.trello.repository.task.LabelRepository;
import uz.md.trello.services.AbstractService;
import uz.md.trello.services.board.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Mon 29/08/22 15:33
 */

@Service
@Transactional
public class LabelService extends AbstractService<
        Label,
        LabelDTO,
        LabelCDTO,
        LabelUDTO,
        Long,
        LabelMapper,
        LabelRepository
        > {

    private final BoardService boardService;

    public LabelService(LabelRepository repository,
                        LabelMapper mapper,
                        BoardService boardService) {
        super(repository, mapper);
        this.boardService = boardService;
    }

    @Override
    public List<LabelDTO> getAll() {
        return null;
    }

    public List<LabelDTO> getAllOf(UUID boardId){
        Board board = boardService.findEntityById(boardId);
        return mapper.toDtos(repository.findAllByBoardId(boardId).orElse(new ArrayList<>()));
    }

    @Override
    public LabelDTO findById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow(()->new GenericNotFoundException("Label not found", 404)));
    }

    @Override
    public LabelDTO create(LabelCDTO labelCDTO, Long createdBy) {
        System.out.println("labelCDTO = " + labelCDTO);
        Label label = mapper.fromCDTO(labelCDTO);
        Board board = boardService.findEntityById(labelCDTO.getBoardId());
        label.setBoard(board);
        Label save = repository.save(label);
        return mapper.toDto(save);
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void softDeleteById(Long aLong) {

    }

    @Override
    public void softDelete(Label entity) {

    }

    @Override
    public LabelDTO update(LabelUDTO labelUDTO) {
        Label label = findEntityById(labelUDTO.getId());
        return mapper.toDto(mapper.fromUDTO(labelUDTO, label));
    }

    @Override
    public Label findEntityById(Long id) {
        return repository.findById(id).orElseThrow(() -> new GenericNotFoundException("Label not found", 404));
    }
}
