package com.tasktracker.tasktrackertaskservice.repository;

import com.tasktracker.tasktrackerentity.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Optional<Status> findByName(String name);
}
