package br.com.andradenathan.todolist.task;

import br.com.andradenathan.todolist.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity(name = "tbl_tasks")
public class Task {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 50)
    private String title;
    private String description;
    private String priority;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private UUID userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
