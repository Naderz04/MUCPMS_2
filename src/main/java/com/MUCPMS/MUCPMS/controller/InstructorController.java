package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.*;
import com.MUCPMS.MUCPMS.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/instructors")
public class InstructorController {

        @Autowired
        private InstructorService instructorService;
        @Autowired
        private StudentsProjectsManagementService studentsProjectsManagementService;
        @Autowired
        private ProjectService projectService;
        @Autowired
        private ProjectIdeaService projectIdeaService;

    private static final Logger logger = LoggerFactory.getLogger(InstructorController.class);


//        @PostMapping
//        public ResponseEntity<ApiResponse<Instructor>> createInstructor(@Valid @RequestBody CreateInstructorDTO instructorDTO) {
//            try {
//                Instructor createdInstructor = instructorService.CreateInstructor(instructorDTO);
//                return ResponseEntity.status(HttpStatus.CREATED)
//                        .body(new ApiResponse<>(true, createdInstructor, "Instructor created successfully", HttpStatus.CREATED.value()));
//            } catch (Exception e) {
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                        .body(new ApiResponse<>(false, null, "Failed to create Instructor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//            }
//        }

    @PostMapping()
    public String createInstructor(@Valid @RequestBody CreateInstructorDTO instructorDTO, Model model) {
        try {
            Instructor createdInstructor = instructorService.CreateInstructor(instructorDTO);
            model.addAttribute("successMessage", "Instructor created successfully!");
            return "instructorHome"; // Redirect to the instructor list page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create instructor: " + e.getMessage());
            return "create-instructor"; // Return to the same page with an error message
        }
    }

    @PostMapping("/createTask")
    public String createTask(
            @ModelAttribute CreateTaskDTO task,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {
        try {
            logger.info("Received file: {}", (file != null ? file.getOriginalFilename() : "No file"));
            logger.info("File empty: {}", (file != null ? file.isEmpty() : "N/A"));
            task.setFile(file);
            task.setInstructorEmail(authentication.getName());
            CreateTaskDTO createdTask = studentsProjectsManagementService.createAndAssignTask(task);
            logger.info("Task created with ID: {}, File path: {}", createdTask.getTaskId(), createdTask.getAttachedFilePath());
            redirectAttributes.addFlashAttribute("message", "Task created successfully!");
            return "redirect:/instructors/instructorTasks";
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error creating task: " + e.getMessage());
            return "redirect:/instructors/instructorTasks";
        }
    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<ViewInstructorDTO>>> getAllInstructors() {
        try {
            // Fetch all instructors from the service layer
            List<Instructor> instructorList = instructorService.getAllInstructors();

            // Map each Instructor model to a ViewInstructorDTO
            List<ViewInstructorDTO> instructorDTOs = instructorList.stream().map(instructor -> {
                // Extract the list of project IDs
                List<Long> projectIds = instructor.getProjects().stream()
                        .map(Project::getProjectId) // Assuming ProjectId is the unique identifier
                        .collect(Collectors.toList());
                List<Long> tasks = instructor.getTasks().stream()
                        .map(Task::getTaskId) // Assuming ProjectId is the unique identifier
                        .collect(Collectors.toList());

                // Create a new ViewInstructorDTO for each instructor
                return new ViewInstructorDTO(
                        instructor.getInstructorEmail(),
                        instructor.getInstructorName(),
                        projectIds,tasks
                );
            }).collect(Collectors.toList());

            // Return the list of DTOs in an ApiResponse
            return ResponseEntity.ok(new ApiResponse<>(true, instructorDTOs, "Instructors retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve Instructors: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }


//    @GetMapping("/{email}")
//    public String getInstructorByEmail(@PathVariable String email, Model model) {
//        try {
//            Instructor instructor = instructorService.getInstructorByEmail(email);
//            if (instructor != null) {
//                model.addAttribute("instructor", instructor);
//                return "view-instructor"; // Return a view page showing instructor details
//            } else {
//                model.addAttribute("errorMessage", "Instructor not found");
//                return "error"; // Show an error page
//            }
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Failed to retrieve instructor: " + e.getMessage());
//            return "error"; // Error page
//        }
//    }

    @GetMapping("/{email}")
    public String getInstructorByEmail(@PathVariable String email, Model model) {
        try {
            Instructor instructor = instructorService.getInstructorByEmail(email);
            if (instructor != null) {
                model.addAttribute("instructor", instructor);
                return "view-instructor"; // Return a view page showing instructor details
            } else {
                model.addAttribute("errorMessage", "Instructor not found");
                return "error"; // Show an error page
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to retrieve instructor: " + e.getMessage());
            return "error"; // Error page
        }
    }

//
//
//    @GetMapping("/{email}")
//    public ResponseEntity<ApiResponse<ViewInstructorDTO>> getInstructorByEmail(@PathVariable String email) {
//        try {
//            Instructor instructor = instructorService.getInstructorByEmail(email);
//
//            if (instructor != null) {
//                // Extract the list of project IDs
//                List<Long> projectIds = instructor.getProjects().stream()
//                        .map(Project::getProjectId) // Assuming ProjectId is the unique identifier
//                        .collect(Collectors.toList());
//
//                // Create the ViewInstructorDTO
//                ViewInstructorDTO viewInstructorDTO = new ViewInstructorDTO(
//                        instructor.getInstructorEmail(),
//                        instructor.getInstructorName(),
//                        projectIds
//                );
//
//                return ResponseEntity.ok(new ApiResponse<>(true, viewInstructorDTO, "Instructor retrieved successfully", HttpStatus.OK.value()));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse<>(false, null, "Instructor not found", HttpStatus.NOT_FOUND.value()));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to retrieve Instructor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }
//



    @PutMapping("/{email}")
    public ResponseEntity<ApiResponse<UpdateInstructorDTO>> updateInstructor(@PathVariable String id, @RequestBody UpdateInstructorDTO updateInstructorDTO) {
        try {
            Instructor instructor = studentsProjectsManagementService.updateInstructor(id, updateInstructorDTO);
            if (instructor != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, updateInstructorDTO, "Instructor updated successfully", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Instructor not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to update Instructor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

        @DeleteMapping("/{email}")
        public ResponseEntity<ApiResponse<Void>> deleteInstructor(@PathVariable String email) {
            try {
                instructorService.DeleteInstructor(email);
                return ResponseEntity.ok(new ApiResponse<>(true, null, "Instructor deleted successfully", HttpStatus.OK.value()));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ApiResponse<>(false, null, "Failed to delete Instructor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
            }
        }

    @GetMapping("/instructor/{email}")
    public ResponseEntity<ApiResponse<List<ViewProjectDTO>>> getProjectsByInstructorEmail(@PathVariable String email) {
        try {
            // Fetch projects by instructor email
            List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(email);

            // Map each Project to a ViewProjectDTO
            List<ViewProjectDTO> projectDTOs = projects.stream().map(project -> {
                // Extract the list of student emails
                List<String> studentsEmails = project.getStudents().stream()
                        .map(Student::getStudentEmail)
                        .collect(Collectors.toList());

                // Extract the task IDs
                List<Long> taskIds = project.getTasks().stream()
                        .map(Task::getTaskId)
                        .collect(Collectors.toList());

                // Create a ViewProjectDTO
                return new ViewProjectDTO(
                        project.getProjectId(),
                        project.getProjectIdea(),
                        project.getProjectDescription(),
                        studentsEmails,
                        project.getInstructor() != null ? project.getInstructor().getInstructorEmail() : null,
                        taskIds
                );
            }).collect(Collectors.toList());

            // Return the list of projects as a response
            return ResponseEntity.ok(new ApiResponse<>(true, projectDTOs, "Projects retrieved successfully", HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve projects: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

//Routingggggggggggggggggggggggggggggggggggggggggggggggggggggggg



    @GetMapping("/projectIdeas")
    public String getProjectIdeas(Model model) {
        List<ProjectIdea> projectIdeas = projectIdeaService.getAllProjectsIdeas();
        model.addAttribute("projectIdeas", projectIdeas);
        // Return the name of the Thymeleaf HTML page (e.g., projectIdeas.html)
        return "projectIdeas";
    }

    @GetMapping("/instructorHome")
    public String instructorHome(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<Task> tasks = studentsProjectsManagementService.getTasksByInstructorEmail(email);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(email);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projects);
        return "instructorHome";

    }
    @GetMapping("/instructorTasks")
    public String getAllTasks(Model model, Authentication authentication) {
        String email = authentication.getName();
        Instructor instructor = instructorService.getInstructorByEmail(email);
        List<Task> taskList = studentsProjectsManagementService.getTasksByInstructorEmail(email);
        model.addAttribute("tasks", taskList);
        return "instructorTasks";
    }

    @GetMapping("/instructorProjects")
    public String instructorProjects(Model model, Authentication authentication) {
        String email = authentication.getName();
        Instructor instructor = instructorService.getInstructorByEmail(email);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(email);
        model.addAttribute("projects", projects);
        return "instructorProjects";
    }

    @GetMapping("/{projectId}/details/{taskId}")
    public String getTaskDetails(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
        try {
            // Fetch task details
            ViewTaskSubmissionDTO taskDetails = studentsProjectsManagementService.getTaskDetails(projectId, taskId);

            // Add task details to the model
            model.addAttribute("taskDetails", taskDetails);
            return "taskDetails"; // Return the name of the view to render task details
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", "Task or project not found: " + e.getMessage());
            return "errorPage";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to retrieve task details: " + e.getMessage());
            return "errorPage";
        }
    }

    @GetMapping("/")
    @RequestMapping
    public String showHomePage() {
        return "home"; // Load index.html as the first page
    }


}


