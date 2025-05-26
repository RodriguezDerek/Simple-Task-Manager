package com.task_manager.backend.controller;

import com.task_manager.backend.model.Task;
import com.task_manager.backend.service.TaskService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/task-manager/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(
            @RequestParam("team") String team,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file
    ){
        Task task = Task.builder()
                .team(team)
                .title(title)
                .description(description)
                .fileKey(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task, file));
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTasks(){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.getAllTasks());
    }

    @GetMapping("/tasks/download/file/{id}")
    public ResponseEntity<byte[]> downloadTaskFile(@NotNull @PathVariable Integer id){
        Task task = taskService.getTaskById(id);
        byte[] fileBytes = taskService.downloadTask(id);
        String filename = task.getFileKey().substring(task.getFileKey().lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("Content-Type", "application/octet-stream")
                .body(fileBytes);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@NotNull @PathVariable Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(taskService.deleteTask(id));
    }
}
