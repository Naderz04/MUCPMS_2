package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.CreateTaskSubmissionDTO;
import com.MUCPMS.MUCPMS.DTO.request.UpdateGradeDTO;
import com.MUCPMS.MUCPMS.DTO.request.ViewTaskSubmissionDTO;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.model.TaskSubmission;
import com.MUCPMS.MUCPMS.service.StudentsProjectsManagementService;
import com.MUCPMS.MUCPMS.service.TaskSubmissionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/task-submissions")
@RequiredArgsConstructor
public class TaskSubmissionController {
    @Autowired
    private final TaskSubmissionService taskSubmissionService;
    @Autowired
    private final StudentsProjectsManagementService studentsProjectsManagementService;



    @PostMapping
    public String createTaskSubmission(
            @RequestParam("taskId") Long taskId,
            @RequestParam("projectId") Long projectId,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            // Create the DTO
            CreateTaskSubmissionDTO dto = new CreateTaskSubmissionDTO();
            dto.setTaskId(taskId);
            dto.setProjectId(projectId);
            dto.setSubmissionFile(file);

            // Save the submission
            taskSubmissionService.createTaskSubmission(dto);

            redirectAttributes.addFlashAttribute("message", "File submitted successfully!");
            return "redirect:/studentHome"; // Redirect to the student home page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit file: " + e.getMessage());
            return "redirect:/studentHome";
        }
    }



    @PostMapping("/grade")
    public String updateGrade(
            @RequestParam("submissionId") Long submissionId,
            @RequestParam("grade") Double grade,
            RedirectAttributes redirectAttributes) {

        try {
            UpdateGradeDTO dto = new UpdateGradeDTO();
            dto.setTaskSubmissionId(submissionId);
            dto.setGrade(grade);

            taskSubmissionService.updateGrade(dto);
            redirectAttributes.addFlashAttribute("success", "Grade updated successfully");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/instructors/${projectId}/details";
    }


    @GetMapping("/{taskId}/submissions")
    public ResponseEntity<ApiResponse<List<ViewTaskSubmissionDTO>>> listSubmissionsForTask(@PathVariable Long taskId) {
        try {
            // Fetch all submissions for the task
            List<ViewTaskSubmissionDTO> submissions = taskSubmissionService.getSubmissionsByTask(taskId);

            // Return success response
            return ResponseEntity.ok(
                    new ApiResponse<>(true, submissions, "Submissions retrieved successfully", HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            // Task not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "Task not found: " + e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            // General error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve submissions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }



}
