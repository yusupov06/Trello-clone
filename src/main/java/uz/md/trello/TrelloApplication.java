package uz.md.trello;

import com.fasterxml.jackson.databind.ser.Serializers;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import uz.md.trello.domains.board.Board;
import uz.md.trello.domains.list.BoardList;
import uz.md.trello.repository.board.BoardRepository;
import uz.md.trello.repository.list.BoardListRepository;
import uz.md.trello.repository.task.TaskRepository;
import uz.md.trello.utils.BaseUtils;

import java.util.Map;
import java.util.UUID;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class TrelloApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrelloApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(BoardRepository boardRepository,
//							 BoardListRepository boardListRepository,
//							 TaskRepository taskRepository) {
//		return (args) -> {
//
//			for (Board board : boardRepository.findAll()) {
//				Map<UUID, Integer> mapBoard = BaseUtils.boardPositionForLists;
//				mapBoard.put(board.getId(), boardListRepository.findAllWhereNotDeletedAndNotArchivedByBoard(board.getId()));
//			}
//			System.out.println("  ================================= " + BaseUtils.boardPositionForLists + " =================================");
//
//			for (BoardList list : boardListRepository.findAll()) {
//				Map<UUID, Integer> mapTask = BaseUtils.positionForTasks;
//				mapTask.put(list.getId(), taskRepository.findAllWhereNotDeletedAndNotArchivedByBoard(list.getId()));
//			}
//			System.out.println("  ================================= " + BaseUtils.positionForTasks + " =================================");
//
//		};
//	}

}
