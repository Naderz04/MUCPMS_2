package com.MUCPMS.MUCPMS.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "Student")
@Getter
@Setter
@NoArgsConstructor
public class Student {


    @Id
    private String studentEmail;
    private String studentPhoto;
    private String studentName;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = true) // Allow null if no project is assigned
    private Project project;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_email")
    private User user;

    public Student(String email, String studentName,String studentPhoto) {
        this.studentEmail =email;
        this.studentName =studentName;
        this.project = null;
        this.studentPhoto=studentPhoto;
    }
}
