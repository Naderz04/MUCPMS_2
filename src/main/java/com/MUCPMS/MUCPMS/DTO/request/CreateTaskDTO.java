package com.MUCPMS.MUCPMS.DTO.request;

import com.MUCPMS.MUCPMS.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CreateTaskDTO {
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private String attachedFilePath; // Keep this as String
    private LocalDate dueDate;
    private String instructorEmail;
    private MultipartFile file; // Add this for file upload

    public CreateTaskDTO(String taskName, String taskDescription, LocalDate dueDate, String instructorEmail, MultipartFile file) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.dueDate = dueDate;
        this.instructorEmail = instructorEmail;
        this.file = file;
    }
}