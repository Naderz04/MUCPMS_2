package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Instructor {

    @Id
    private String instructorEmail;

    private String instructorName;
    @OneToMany(mappedBy = "instructor", cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "suggestedBy", cascade = CascadeType.PERSIST) // Do not delete students when removed from list
    private List<ProjectIdea> projectIdeas=new ArrayList<>();

    @OneToMany(mappedBy = "assignedBy", cascade = CascadeType.PERSIST) // Do not delete students when removed from list
    private List<Task> tasks=new ArrayList<>();

    @OneToOne
    @MapsId
    @JoinColumn(name = "instructor_email")
    private User user;

    public Instructor(String email, String instructorName) {
        this.instructorEmail = email;
        this.instructorName = instructorName;
        this.projects=new ArrayList<>();
        this.projectIdeas=new ArrayList<>();
        this.tasks=new ArrayList<>();
    }


    public int getTotalProjectsForInstructorCount() {
        return this.projects.size();
    }

    public int getTotalTasksCount() {
        return this.tasks.size();
    }
    // Getters and Setters


    public int getCompletedProjectsForInstructor(Task task) {
        Set<Project> instructorProjects = new HashSet<>(this.projects); // Projects supervised by the instructor
        Set<Project> submittedProjects = task.getSubmissions().stream()
                .map(TaskSubmission::getProject)
                .collect(Collectors.toSet()); // Projects that submitted the task

        // Find the intersection of both sets
        return (int) submittedProjects.stream()
                .filter(instructorProjects::contains)
                .count();
    }

}