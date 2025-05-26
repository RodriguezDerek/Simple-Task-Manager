package com.task_manager.backend.repository;

import com.task_manager.backend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    boolean existsByTeamAndTitle(String team, String title);
}
