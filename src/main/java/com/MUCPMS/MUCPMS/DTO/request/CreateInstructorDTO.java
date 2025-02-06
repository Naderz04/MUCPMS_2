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
@NoArgsConstructor
@AllArgsConstructor
public class CreateInstructorDTO {
    @Email
    @NotBlank
    private String instructorEmail;
    @NotBlank
    @Size(min = 3, message = "Name should have at least 2 characters")
    private String instructorName;


}