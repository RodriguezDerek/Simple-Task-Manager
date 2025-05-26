package com.task_manager.backend.service;

import com.task_manager.backend.DTO.GenericResponse;
import com.task_manager.backend.S3.S3Service;
import com.task_manager.backend.exception.S3DownloadException;
import com.task_manager.backend.exception.S3UploadException;
import com.task_manager.backend.exception.TaskAlreadyExistsException;
import com.task_manager.backend.exception.TaskDoesNotExistException;
import com.task_manager.backend.model.Task;
import com.task_manager.backend.repository.TaskRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final S3Service s3Service;
    private final String bucketName = "sb-derek-task-manager";

    public GenericResponse createTask(Task task, MultipartFile file) {
        if(taskRepository.existsByTeamAndTitle(task.getTeam(), task.getTitle())) {
            throw new TaskAlreadyExistsException("Task already exists.");
        }

        String key = "tasks/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();

        try{
            s3Service.putObject(bucketName, key, file.getBytes());
        } catch(IOException e){
            throw new S3UploadException("Failed to upload file to S3");
        }

        task.setFileKey(key);
        taskRepository.save(task);

        return GenericResponse.builder()
                .message("Task successfully created.")
                .status(HttpStatus.CREATED.value())
                .task(task)
                .build();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public GenericResponse deleteTask(Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isPresent()){
            s3Service.deleteObject(bucketName, optionalTask.get().getFileKey());
            taskRepository.delete(optionalTask.get());
            return GenericResponse.builder()
                    .message("Task successfully deleted.")
                    .status(HttpStatus.OK.value())
                    .build();
        }

        throw new TaskDoesNotExistException("Task does not exist.");
    }

    public byte[] downloadTask(@NotNull Integer id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if(optionalTask.isEmpty()){
            throw new TaskDoesNotExistException("Task does not exist.");
        }

        try{
            return s3Service.getObject(bucketName, optionalTask.get().getFileKey());

        } catch(Exception e){
            throw new S3DownloadException("Failed to download file from S3");
        }
    }

    public Task getTaskById(Integer id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskDoesNotExistException("Task does not exist."));
    }
}
