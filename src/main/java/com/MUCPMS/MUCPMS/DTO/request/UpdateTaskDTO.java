package com.MUCPMS.MUCPMS.DTO.request;


import java.time.LocalDate;

public class UpdateTaskDTO {

    private String taskName; // e.g., "Select Supervisor", "Submit Functional Requirements"
    private String taskDescription;
    private LocalDate dueDate;

}
