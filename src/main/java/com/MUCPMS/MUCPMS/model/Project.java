package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Project")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String projectIdea;
    @Column(columnDefinition = "TEXT")
    private String projectDescription;

    @OneToMany(mappedBy = "project", cascade = CascadeType.PERSIST, orphanRemoval = false) // Do not delete students when removed from list
    private List<Student> students = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY) // Do not delete instructors when removed from list
    @JoinTable(
            name = "project_instructors", // Name of the join table
            joinColumns = @JoinColumn(name = "project_id"), // Foreign key for Project
            inverseJoinColumns = @JoinColumn(name = "instructor_email") // Foreign key for Instructor
    )
    private Instructor instructor;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "project_tasks", // Join table to connect projects and tasks
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks = new ArrayList<>(); // Many tasks for each project
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskSubmission> taskSubmissions = new ArrayList<>();
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notes> notes= new ArrayList<>();
    private String status;


    public Project(String projectIdea, String projectDescription,Instructor instructor) {
        this.projectIdea = projectIdea;
        this.projectDescription = projectDescription;
        this.students = new ArrayList<>();
        this.instructor = instructor;
        this.tasks=new ArrayList<>();
        this.taskSubmissions=new ArrayList<>();
        this.notes=new ArrayList<>();
    }

    public Project(String projectIdea, String projectDescription,Instructor instructor,List<Task> tasks) {
        this.projectIdea = projectIdea;
        this.projectDescription = projectDescription;
        this.students = new ArrayList<>();
        this.instructor = instructor;
        this.tasks=tasks;
    }

    // Getters and Setters
}