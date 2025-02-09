package com.MUCPMS.MUCPMS.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class ViewTaskSubmissionDTO {
        private String taskName;
        private String projectName;
        private String submissionFilePath;
        private Double grade;
        private LocalDate submissionDate;
    }


