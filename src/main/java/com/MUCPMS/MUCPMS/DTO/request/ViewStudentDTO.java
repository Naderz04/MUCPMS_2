package com.MUCPMS.MUCPMS.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ViewStudentDTO {

    private String studentEmail;
    private String studentName;
    private Long projectId;

}
