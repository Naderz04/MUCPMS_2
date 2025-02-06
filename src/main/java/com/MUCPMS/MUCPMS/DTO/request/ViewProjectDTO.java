package com.MUCPMS.MUCPMS.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewProjectDTO {
    private Long projectId;
    private String projectIdea;
    private String projectDescription;
    private List<String> studentsEmails;
    private String instructorEmail;
    private List<Long> tasksids;
}
