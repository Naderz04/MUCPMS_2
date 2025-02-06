package com.MUCPMS.MUCPMS.DTO.request;

import com.MUCPMS.MUCPMS.model.Instructor;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectIdeaDTO {
    @NotBlank

    private String projectTitle;
    @NotBlank

    private String type;
    @NotBlank

    private String projectDescription;
    @NotBlank

    private String availability;

    private String suggestedByEmail;

}
