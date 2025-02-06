package com.MUCPMS.MUCPMS.DTO.request;

import com.MUCPMS.MUCPMS.model.Instructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectIdeaDTO {

    private String projectTitle;
    private String type;
    private String projectDescription;
    private String Availability;

}
