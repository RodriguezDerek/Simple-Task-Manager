package com.task_manager.backend.exception;

public class TaskDoesNotExistException extends RuntimeException {
    public TaskDoesNotExistException(String message) {
        super(message);
    }
}
