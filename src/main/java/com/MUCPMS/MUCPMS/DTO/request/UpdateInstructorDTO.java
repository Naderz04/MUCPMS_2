package com.MUCPMS.MUCPMS.DTO.request;


import com.MUCPMS.MUCPMS.model.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateInstructorDTO {

    private String instructorName;
    private List<Long> projectsId;
    private List<Long> projectsIdeaId;
}
