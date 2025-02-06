package com.MUCPMS.MUCPMS.DTO.request;


import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Student;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDTO {
    @NotBlank
    private String ProjectIdea;
    @NotBlank
    @Size(min = 5, message = "Project Description should have at least 5 characters")
    private String projectDescription;
    private String instructorEmail;

}
