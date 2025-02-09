package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
    @Table(name = "task_submissions")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
public class TaskSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;


    private Double grade; // Nullable until set by the instructor.

    private LocalDate submissionDate;

    private String submissionFilePath; // Path to the submitted file


    public TaskSubmission(Task task, Project project, LocalDate submissionDate, String submissionFilePath) {
        this.task = task;
        this.project = project;
        this.grade = null;
        this.submissionDate = submissionDate;
        this.submissionFilePath = submissionFilePath;
    }
}


