package com.tasktracker.tasktrackerentity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    private String description;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    @Column(name = "user_id")
    private String userId;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
