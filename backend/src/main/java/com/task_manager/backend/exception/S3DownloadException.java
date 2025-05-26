package com.task_manager.backend.exception;

public class S3DownloadException extends RuntimeException {
    public S3DownloadException(String message) {
        super(message);
    }
}
