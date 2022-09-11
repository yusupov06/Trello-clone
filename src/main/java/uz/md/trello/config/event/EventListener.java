package uz.md.trello.config.event;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.domains.list.Comment;
import uz.md.trello.domains.list.Task;
import uz.md.trello.dtos.board.list.BoardListMoveDTO;
import uz.md.trello.dtos.task.TaskCopyDTO;
import uz.md.trello.dtos.task.TaskMoveDTO;
import uz.md.trello.repository.list.BoardListRepository;
import uz.md.trello.repository.task.TaskRepository;
import uz.md.trello.services.board.BoardService;
import uz.md.trello.services.board.list.BoardListService;
import uz.md.trello.services.task.TaskService;
import uz.md.trello.utils.BaseUtils;
import uz.md.trello.utils.Privacy;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Me: muhammadqodir
 * Project: Trello/IntelliJ IDEA
 * Date:Thu 01/09/22 22:20
 */
@Component
public class EventListener {

    private final BoardService boardService;
    private final BoardListService boardListService;
    private final TaskService taskService;
    private final TaskRepository taskRepository;
    private final BoardListRepository boardListRepository;
    private final Privacy privacy;
    private final BaseUtils baseUtils;

    public EventListener(@Lazy BoardService boardService,
                         @Lazy BoardListService boardListService,
                         @Lazy TaskService taskService,
                         @Lazy TaskRepository taskRepository,
                         @Lazy BoardListRepository boardListRepository,
                         @Lazy Privacy privacy,
                         @Lazy BaseUtils baseUtils) {
        this.boardService = boardService;
        this.boardListService = boardListService;
        this.taskService = taskService;
        this.taskRepository = taskRepository;
        this.boardListRepository = boardListRepository;
        this.privacy = privacy;
        this.baseUtils = baseUtils;
    }

    @org.springframework.context.event.EventListener
    public void moveListListener(@NotNull BoardListMoveDTO moveDTO) {
        System.out.println(" ==================================== List Move Listener ============================================== ");
        BoardList boardList = boardListService.findEntityByIdNotArchived(moveDTO.getBoardListId());
        Board board = boardService.findEntityById(moveDTO.getBoardId());


        if (boardList.getBoard().getId().equals(board.getId())){

            privacy.checkForValidPositionForList(moveDTO.getPosition(), board.getId());
            moveThisList(boardList, moveDTO.getPosition(), board.getId());

        } else {
            boardList.setBoard(board);
            boardList.setListOrder(baseUtils.getPositionList(board.getId()));
            moveThisList(boardList, moveDTO.getPosition(), board.getId());

        }



    }

    private void moveThisList(BoardList boardList, int moveTo, UUID boardId){

        int from = boardList.getListOrder(), to = moveTo;
        if (from == to) return;

        System.out.println("from = " + from);
        System.out.println("to = " + to);


        if (from < to) {

            boardListRepository
                    .moveBack(from, to, boardId);
            boardListRepository.changePosition(boardList.getId(), moveTo);

        } else {

            boardListRepository
                    .moveForward(to, from, boardId);
            boardListRepository.changePosition(boardList.getId(), moveTo);

        }

    }


    @org.springframework.context.event.EventListener /*(condition = "#taskMoveDTOGenericEvent.success")*/
    public void moveTaskListener(TaskMoveDTO moveDTO) {
        System.out.println(" ==================================== Task Move Listener ============================================== ");

        System.out.println(" =================================== ");
        System.out.println(moveDTO.getListId());
        System.out.println(moveDTO.getTaskId());
        System.out.println(moveDTO.getPosition());
        System.out.println(" =================================== ");

        Task task = taskService.findEntityByIdNotArchived(moveDTO.getTaskId());
        BoardList boardList = boardListService.findEntityByIdNotArchived(moveDTO.getListId());

        if (boardList.getId().equals(task.getBoardList().getId())) {

            privacy.checkForValidPositionForTask(moveDTO.getPosition(), boardList.getId());
            moveThisTask(task, moveDTO.getPosition(), boardList);

        } else {
            privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),
                    "Permission denied. You are not allowed to this board in this workspace");

            taskRepository.changePositionsBack(task.getPosition(), task.getBoardList().getId());



            task.setBoardList(boardList);
            task.setPosition(baseUtils.getPositionTask(boardList.getId()));
            System.out.println("task.getPosition() = " + task.getPosition());
            System.out.println("boardList.getTasks() = " + boardList.getTasks());

            moveThisTask(task, moveDTO.getPosition(), boardList);

        }


    }

    private void moveThisTask(@NotNull Task task, int position, BoardList boardList) {


        int from = task.getPosition() , to = position;
        System.out.println("from = " + from);
        System.out.println("to = " + to);
        if (from == to) return;



        if (from < to) {

            taskRepository
                    .moveBack(from, to, boardList.getId());
            taskRepository.changePosition(task.getId(), position);

        } else {

            taskRepository
                    .moveForward(to, from, boardList.getId());
            taskRepository.changePosition(task.getId(), position);
        }

    }


    @org.springframework.context.event.EventListener /*(condition = "#taskCopyDTOGenericEvent.success")*/
    public void copyTaskListener(TaskCopyDTO copyDTO) {
        System.out.println(" ==================== Task copy listener ==================== ");
        Task task = taskService.findEntityById(copyDTO.getTaskId());

        Task copiedTask = new Task(task);
        copiedTask.setName(copyDTO.getTitle());
        for (Comment comment : copiedTask.getComments()) {
            comment.setTask(copiedTask);
        }
        if (!copyDTO.isKeepLabels()) {
            copiedTask.setLabels(new ArrayList<>());
        }
        System.out.println("copyDTO = " + copyDTO);
        setTheseToTask(copiedTask,  copyDTO.getListId(), copyDTO.getPosition());
    }
    private void setTheseToTask(Task task, UUID listId, Integer position) {

        System.out.println(" ============================== " );
        System.out.println(listId);
        System.out.println(position);
        System.out.println(" ============================== " );

        BoardList boardList = boardListService.findEntityByIdNotArchived(listId);
        privacy.checkForMemberPermissionIfFoundGet(boardList.getBoard(),
                "Permission denied. You are not allowed to this board");

        task.setBoardList(boardList);
        task.setPosition(baseUtils.getPositionTask(boardList.getId()));
        Task save = taskRepository.save(task);
        System.out.println("save = " + save);
        moveThisTask(save, position, boardList);

    }

}
