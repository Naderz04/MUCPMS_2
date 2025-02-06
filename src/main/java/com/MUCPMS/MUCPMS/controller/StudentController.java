package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.CreateStudentDTO;
//import com.MUCPMS.DTO.request.UpdateCourseDTO;
//import com.MUCPMS.MUCPMS.DTO.request.UpdateStudentDTO;
import com.MUCPMS.MUCPMS.DTO.request.CreateTaskSubmissionDTO;
import com.MUCPMS.MUCPMS.DTO.request.UpdateStudentDTO;
import com.MUCPMS.MUCPMS.DTO.request.ViewStudentDTO;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Student;
//import com.MUCPMS.MUCPMS.service.ManagementService;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.service.ProjectService;
import com.MUCPMS.MUCPMS.service.StudentService;
import com.MUCPMS.MUCPMS.service.StudentsProjectsManagementService;
import com.MUCPMS.MUCPMS.service.TaskSubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentsProjectsManagementService studentsProjectsManagementService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TaskSubmissionService taskSubmissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<Student>> createStudent(@Valid @RequestBody CreateStudentDTO studentDTO) {
        try {
            Student createdStudents = studentService.CreateStudent(studentDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, createdStudents, "Student created successfully", HttpStatus.CREATED.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to create student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<ViewStudentDTO>>> getAllStudents() {
        try {
            // Fetch all students from the service layer
            List<Student> studentList = studentService.getAllStudents();

            // Map each Student model to a ViewStudentDTO
            List<ViewStudentDTO> studentDTOs = studentList.stream().map(student -> {
                // Handle null project safely
                Long projectId = (student.getProject() != null) ? student.getProject().getProjectId() : null;

                // Create a new ViewStudentDTO for each student
                return new ViewStudentDTO(
                        student.getStudentName(),
                        student.getStudentEmail(),
                        projectId
                );
            }).collect(Collectors.toList());

            // Return the list of DTOs in an ApiResponse
            return ResponseEntity.ok(new ApiResponse<>(true, studentDTOs, "Students retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve students: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse<ViewStudentDTO>> getStudentByEmail(@PathVariable String email) {
        try {
            Student student = studentService.getStudentByEmail(email);

            if (student != null) {
                // Handle null project safely
                Long projectId = (student.getProject() != null) ? student.getProject().getProjectId() : null;

                // Create ViewStudentDTO
                ViewStudentDTO viewStudentDTO = new ViewStudentDTO(
                        student.getStudentName(),
                        student.getStudentEmail(),
                        projectId
                );

                return ResponseEntity.ok(new ApiResponse<>(true, viewStudentDTO, "Student retrieved successfully", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Student not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UpdateStudentDTO>> updateStudent(@PathVariable String email, @RequestBody UpdateStudentDTO updatedStudent) {
        try {
            Student student = studentsProjectsManagementService.updateStudent(email, updatedStudent);
            if (student != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, updatedStudent, "Student updated successfully", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Student not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to update student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<ApiResponse<Void>> deleteStudent(@PathVariable String email) {
        try {
            studentsProjectsManagementService.deleteStudent(email);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "Student deleted successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to delete student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


//    @GetMapping("/studentHome")
//    public String StudentHome(Model model, Authentication authentication) {
//        String email = authentication.getName();
//        List<Task> tasks = studentsProjectsManagementService.getTasksByInstructorEmail(email);
//        model.addAttribute("tasks", tasks);
//        return "studentHome";
//
//    }

    @GetMapping("/studentHome")
    public String getTasksForProject(Model model, Authentication authentication) {
        // Fetch the project
        Student s=studentService.getStudentByEmail(authentication.getName());
        Long projectId=s.getProject().getProjectId();
        Project project = projectService.findById(projectId);
//                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Fetch all tasks assigned to the project
        List<Task> tasks = studentsProjectsManagementService.getTasksByInstructorEmail(project.getInstructor().getInstructorEmail());

        // Classify tasks based on their status for the specific project
        List<Task> assignedTasks = tasks.stream()
                .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Assigned"))
                .collect(Collectors.toList());

        List<Task> missingTasks = tasks.stream()
                .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Missing"))
                .collect(Collectors.toList());

        List<Task> doneTasks = tasks.stream()
                .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Done"))
                .collect(Collectors.toList());

        // Pass the classified tasks to the Thymeleaf template
        model.addAttribute("assignedTasks", assignedTasks);
        model.addAttribute("missingTasks", missingTasks);
        model.addAttribute("doneTasks", doneTasks);
        model.addAttribute("project", project);

        return "studentHome"; // This should match your Thymeleaf template name
    }



    @PostMapping("/submitTask")
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
            return "redirect:/students/studentHome"; // Redirect to the student home page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit file: " + e.getMessage());
            return "redirect:/students/studentHome";
        }
    }
  }