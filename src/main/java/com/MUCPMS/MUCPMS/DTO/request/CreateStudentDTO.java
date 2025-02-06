package com.MUCPMS.MUCPMS.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateStudentDTO {
    @Email
    String StudentEmail;
    @NotBlank
    @Size(min = 2, message = "Name should have at least 2 characters")
    String StudentName;

}
