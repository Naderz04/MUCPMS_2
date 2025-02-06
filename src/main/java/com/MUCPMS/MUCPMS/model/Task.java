package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskName; // e.g., "Select Supervisor", "Submit Functional Requirements"
    private String taskDescription;
    @ManyToMany(mappedBy = "tasks")
    private List<Project> projects = new ArrayList<>(); // Many projects can share the same task

    private LocalDate dueDate;
    private String attachedFilePath; // This can be null if no file is attached

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskSubmission> submissions = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "instructor_email")
    private Instructor assignedBy;

    public Task(String taskName, String taskDescription, LocalDate dueDate,Instructor assignedBy,String attachedFilePath) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.projects = new ArrayList<>();
        this.dueDate = dueDate;
        this.assignedBy=assignedBy;
        this.attachedFilePath = attachedFilePath;
    }

    public int getTotalProjectsCount() {
        return this.projects.size();
    }

    public int getCompletedTasksCount() {
        return (int) this.submissions.stream()
                .map(TaskSubmission::getProject) // Get the project from each submission
                .distinct() // Ensure each project is only counted once
                .count();
    }

    // Default constructor and other methods
}


