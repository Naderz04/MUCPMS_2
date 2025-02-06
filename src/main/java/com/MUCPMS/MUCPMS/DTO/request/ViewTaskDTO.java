package com.MUCPMS.MUCPMS.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewTaskDTO {
    private Long taskId;
    private String taskName; // e.g., "Select Supervisor", "Submit Functional Requirements"
    private String taskDescription;
    private String attachedFilePath;
    private LocalDate dueDate;


}
