package br.com.andradenathan.todolist.task;

import br.com.andradenathan.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody Task taskData, HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskData.getStartedAt()) || currentDate.isAfter(taskData.getEndedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O usuário não pode inserir uma data menor do que a atual");
        }

        if(taskData.getStartedAt().isAfter(taskData.getEndedAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de início deve ser maior que a de fim");
        }

        taskData.setUserId((UUID) userId);

        var task = this.taskRepository.save(taskData);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @GetMapping("/")
    public List<Task> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var userTasks = this.taskRepository.findByUserId((UUID) userId);
        return userTasks;
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody Task taskData, @PathVariable UUID id, HttpServletRequest request) {
        var task = this.taskRepository.findById(id).orElse(null);
        var userId = request.getAttribute("userId");

        if(task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tarefa não encontrada");
        }

        if(!task.getUserId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário sem permissão para alterar a tarefa");
        }

        Utils.copyNonNullProperties(taskData, task);
        return ResponseEntity.status(HttpStatus.OK).body(this.taskRepository.save(task));
    }
}
