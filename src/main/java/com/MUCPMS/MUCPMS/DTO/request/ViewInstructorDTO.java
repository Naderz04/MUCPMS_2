package com.MUCPMS.MUCPMS.DTO.request;

import com.MUCPMS.MUCPMS.model.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewInstructorDTO {


    private String instructorEmail;
    private String instructorName;
    private List<Long> projectsId;
    private List<Long> tasksId;

}
