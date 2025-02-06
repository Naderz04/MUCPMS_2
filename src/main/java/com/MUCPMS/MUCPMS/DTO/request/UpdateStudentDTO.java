package com.MUCPMS.MUCPMS.DTO.request;


import com.MUCPMS.MUCPMS.model.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentDTO {

    private String studentEmail;
    private String studentName;
    private Long projectId;

}
