package com.MUCPMS.MUCPMS.model;

import com.MUCPMS.MUCPMS.model.Project;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssessmentGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    // Report (25%)
    private Double reportSupervisor;

    // CSC 498 (10%)
    private Double csc498Supervisor;

    // Presentation (15%)
    private Double presentationSupervisor;
    private Double presentationCommittee1;
    private Double presentationCommittee2;

    // Implementation (40%)
    private Double implementationSupervisor;
    private Double implementationCommittee1;
    private Double implementationCommittee2;

    // Extra (10%)
    private Double extraSupervisor;
    private Double extraCommittee1;
    private Double extraCommittee2;

    public AssessmentGrade(Project project) {
        this.project = project;
    }
}

