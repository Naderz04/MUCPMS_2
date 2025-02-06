package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ProjectIdea")
@Getter
@Setter
@NoArgsConstructor
public class ProjectIdea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectIdeaId;
    private String projectTitle;
    private String type;
    private String projectDescription;
    private String availability;
    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = true)
    private Instructor suggestedBy;

    public ProjectIdea(String projectTitle,String type, String projectDescription, String availability,Instructor suggestedBy) {
        this.projectTitle = projectTitle;
        this.type=type;
        this.projectDescription = projectDescription;
        this.availability = availability;
        this.suggestedBy= suggestedBy;
    }
}
