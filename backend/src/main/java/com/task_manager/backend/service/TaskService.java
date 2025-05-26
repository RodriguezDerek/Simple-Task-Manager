package com.task_manager.backend.service;

import com.task_manager.backend.DTO.GenericResponse;
import com.task_manager.backend.exception.TaskAlreadyExistsException;
import com.task_manager.backend.exception.TaskDoesNotExistException;
import com.task_manager.backend.model.Task;
import com.task_manager.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public GenericResponse createTask(Task task) {
        if(!taskRepository.existsByTeamAndTitle(task.getTeam(), task.getTitle())){
            taskRepository.save(task);
            return GenericResponse
                    .builder()
                    .message("Task successfully created.")
                    .status(HttpStatus.CREATED.value())
                    .task(task)
                    .build();
        }

        throw new TaskAlreadyExistsException("Task already exists.");
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public GenericResponse updateTask(Task task, Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            Task presentTask = optionalTask.get();
            presentTask.setTeam(task.getTeam());
            presentTask.setTitle(task.getTitle());
            presentTask.setDescription(task.getDescription());
            presentTask.setDueDate(task.getDueDate());
            presentTask.setFileURL(task.getFileURL());
            taskRepository.save(presentTask);

            return GenericResponse
                    .builder()
                    .message("Task successfully updated.")
                    .status(HttpStatus.OK.value())
                    .task(presentTask)
                    .build();
        }

        throw new TaskDoesNotExistException("Task does not exist.");
    }

    public GenericResponse deleteTask(Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            taskRepository.delete(optionalTask.get());
            return GenericResponse.builder()
                    .message("Task successfully deleted.")
                    .status(HttpStatus.OK.value())
                    .build();
        }

        throw new TaskDoesNotExistException("Task does not exist.");
    }
}
