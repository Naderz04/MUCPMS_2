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
public class ViewProjectIdeaDTO {


    private String projectTitle;
    private String Type;
    private String projectDescription;
    private String Availability;
    private String SuggestedByEmail;

}
