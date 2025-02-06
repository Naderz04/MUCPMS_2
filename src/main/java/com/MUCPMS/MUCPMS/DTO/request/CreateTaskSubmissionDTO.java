package com.MUCPMS.MUCPMS.DTO.request;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskSubmissionDTO {

    private Long taskId;
    private Long projectId;
    private MultipartFile submissionFile;
    @Temporal(TemporalType.TIMESTAMP)
    private Date submissionDate;    // Path to the submitted file

    public CreateTaskSubmissionDTO(Long taskId, Long projectId, MultipartFile submissionFile) {
        this.taskId = taskId;
        this.projectId = projectId;
        this.submissionFile = submissionFile;
    }
}
