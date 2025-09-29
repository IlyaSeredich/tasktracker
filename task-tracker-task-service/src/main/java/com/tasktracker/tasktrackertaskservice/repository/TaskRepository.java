package com.tasktracker.tasktrackertaskservice.repository;

import com.tasktracker.tasktrackerentity.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

}
