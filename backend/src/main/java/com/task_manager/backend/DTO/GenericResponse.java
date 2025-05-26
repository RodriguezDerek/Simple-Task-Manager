package com.task_manager.backend.DTO;

import com.task_manager.backend.model.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse {
    private String message;
    private int status;
    private Task task;
}
