package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Student;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.service.StudentsProjectsManagementService;
import com.MUCPMS.MUCPMS.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private StudentsProjectsManagementService studentsProjectsManagementService;
    @Autowired
    private TaskService taskService;



    @PostMapping("/createTask")
    public String createTask(
            @ModelAttribute CreateTaskDTO task,
            RedirectAttributes redirectAttributes,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            // Ensure the task is being created by the logged-in instructor
            task.setInstructorEmail(email); // Assuming CreateTaskDTO has a setter for instructorEmail
            studentsProjectsManagementService.createAndAssignTask(task);
            redirectAttributes.addFlashAttribute("message", "Task created successfully!");
            return "redirect:/instructorTasks";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating task: " + e.getMessage());
            return "redirect:/instructorTasks";
        }
    }


    @DeleteMapping("/deleteAll")
    public ResponseEntity<ApiResponse<Void>> deleteAllTasks() {
        try {
            taskService.deleteAllTasks();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, null, "All Tasks deleted successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to delete all tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @GetMapping("/{instructorEmail}")
    public ResponseEntity<ApiResponse<List<ViewTaskDTO>>> getAllTasks(@PathVariable String instructorEmail) {
        try {
            if (instructorEmail == null || instructorEmail.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>(false, null, "Instructor email is required", HttpStatus.BAD_REQUEST.value()));
            }

            // Fetch tasks assigned by this instructor from the service layer
            List<Task> taskList = studentsProjectsManagementService.getTasksByInstructorEmail(instructorEmail);

            // Map each Task model to a ViewTaskDTO
            List<ViewTaskDTO> taskDTOs = taskList.stream().map(task -> {
                return new ViewTaskDTO(
                        task.getTaskId(),
                        task.getTaskName(),
                        task.getTaskDescription(),
                        task.getAttachedFilePath(),
                        task.getDueDate()
                );
            }).collect(Collectors.toList());

            // Return the list of DTOs in an ApiResponse
            return ResponseEntity.ok(new ApiResponse<>(true, taskDTOs, "Tasks retrieved successfully", HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, "No tasks found: " + e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


//    @GetMapping("/{projectId}/details/{taskId}")
//    public ResponseEntity<ApiResponse<ViewTaskSubmissionDTO>> getTaskDetails(
//            @PathVariable Long projectId,
//            @PathVariable Long taskId) {
//        try {
//            // Fetch the task details
//            ViewTaskSubmissionDTO taskDetails = studentsProjectsManagementService.getTaskDetails(projectId, taskId);
//
//            // Return the task details wrapped in an ApiResponse
//            return ResponseEntity.ok(
//                    new ApiResponse<>(true, taskDetails, "Task details retrieved successfully", HttpStatus.OK.value()));
//
//        } catch (EntityNotFoundException e) {
//            // Task or project not found
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, null, "Task or project not found: " + e.getMessage(), HttpStatus.NOT_FOUND.value()));
//        } catch (Exception e) {
//            // General error
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to retrieve task details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }


}


