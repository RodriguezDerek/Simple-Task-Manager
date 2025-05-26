package com.task_manager.backend.controller;

import com.task_manager.backend.model.Task;
import com.task_manager.backend.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task-manager/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(@Valid @RequestBody Task task){
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<?> updateTask(@Valid @RequestBody Task task, @NotNull @PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(task, id));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@NotNull @PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteTask(id));
    }
}
