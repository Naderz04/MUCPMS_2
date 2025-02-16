package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.*;
import com.MUCPMS.MUCPMS.repository.AssessmentSheetRepository;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import com.MUCPMS.MUCPMS.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        @Autowired
        private AssessmentService assessmentService;
        @Autowired
        private TaskSubmissionService taskSubmissionService;

        @Autowired
        private ProjectRepository projectRepository;

        private static final Logger logger = LoggerFactory.getLogger(InstructorController.class);



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
    @GetMapping("/instructorProgressPage")
    public String getProjects(Model model,Authentication authentication) {

        String email = authentication.getName();
        Instructor instructor=instructorService.getInstructorByEmail(email);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(authentication.getName());

        model.addAttribute("instructor", instructor);

        model.addAttribute("allProjects", projects);
        model.addAttribute("lateProjects", projectRepository.findByStatus("late"));
        model.addAttribute("onProgressProjects", projectRepository.findByStatus("onProgress"));
        model.addAttribute("almostCompletedProjects", projectRepository.findByStatus("almostCompleted"));
        model.addAttribute("completedProjects", projectRepository.findByStatus("completed"));

        return "instructorProgressPage"; // projects.html Thymeleaf template
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<?> updateProjectStatus(@RequestBody Map<String, String> payload) {
        // Expecting payload with "projectId" and "status"
        Long projectId = Long.valueOf(payload.get("projectId"));
        String newStatus = payload.get("status");

        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (!projectOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Project project = projectOpt.get();
        project.setStatus(newStatus);
        projectRepository.save(project);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/instructorHome")
    public String instructorHome(Model model, Authentication authentication) {
        String email = authentication.getName();
        Instructor instructor=instructorService.getInstructorByEmail(email);
        List<Task> tasks = studentsProjectsManagementService.getTasksByInstructorEmail(email);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(email);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projects);
        model.addAttribute("instructor", instructor);

        return "instructorHome";

    }
    @GetMapping("/instructorTasks")
    public String getAllTasks(Model model, Authentication authentication) {
        String email = authentication.getName();

        Instructor instructor = instructorService.getInstructorByEmail(email);
        List<Task> taskList = studentsProjectsManagementService.getTasksByInstructorEmail(email);


        model.addAttribute("tasks", taskList);

        model.addAttribute("instructor", instructor);
        return "instructorTasks";
    }

    @GetMapping("/instructorProjects")
    public String instructorProjects(Model model, Authentication authentication) {
        String email = authentication.getName();
        Instructor instructor = instructorService.getInstructorByEmail(email);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(email);
        model.addAttribute("projects", projects);
        model.addAttribute("instructor", instructor);
        return "instructorProjects";
    }

        @GetMapping("/{projectId}/details")
        public String getProjectDetails(@PathVariable Long projectId, Model model) {
            Project project = projectService.getProjectById(projectId);



            // Fetch tasks assigned to the project
            List<Task> tasks = studentsProjectsManagementService
                    .getTasksByInstructorEmail(project.getInstructor().getInstructorEmail());

            // Get all task submissions for the project
            Map<Long, TaskSubmission> taskSubmissions = taskSubmissionService.getTaskSubmissionsForProject(projectId);

            // Classify tasks based on their status
            List<Task> assignedTasks = tasks.stream()
                    .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Assigned"))
                    .collect(Collectors.toList());

            List<Task> missingTasks = tasks.stream()
                    .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Missing"))
                    .collect(Collectors.toList());

            List<Task> doneTasks = tasks.stream()
                    .filter(task -> studentsProjectsManagementService.getStatusForProject(task, project).equals("Done"))
                    .collect(Collectors.toList());

            // Pass data to the Thymeleaf template
            model.addAttribute("assignedTasks", assignedTasks);
            model.addAttribute("missingTasks", missingTasks);
            model.addAttribute("doneTasks", doneTasks);
            model.addAttribute("taskSubmissions", taskSubmissions);
            model.addAttribute("project", project);

            return "projectDetails"; // Your Thymeleaf template name
        }


    @GetMapping("/{projectId}/details/{taskId}")
    public String getTaskDetails(@PathVariable Long projectId, @PathVariable Long taskId, Model model) {
        try {
            // Fetch task details
            ViewTaskSubmissionDTO taskDetails = studentsProjectsManagementService.getTaskDetails(projectId, taskId);
            List<ViewTaskSubmissionDTO> taskSubmissions=taskSubmissionService.getSubmissionsByTask(taskId);
            // Add task details to the model
            model.addAttribute("selectedTaskSubmissions",taskSubmissions);
            model.addAttribute("taskDetails", taskDetails);
            model.addAttribute("project", projectService.getProjectById(projectId));
            model.addAttribute("taskSubmissions", taskSubmissions);
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

    @GetMapping("/{email}/photo")
    public ResponseEntity<Resource> getInstructorPhoto(@PathVariable String email) {
        Instructor instructor = instructorService.getInstructorByEmail(email);
        if (instructor == null || instructor.getInstructorPhoto() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get("uploads/" + instructor.getInstructorPhoto());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/assessmentSheet")
    public String showAssessmentPage(Model model) {
        // This will get all projects with their assessments (creating new ones if needed)
        model.addAttribute("assessments", assessmentService.getAllAssessmentsWithProjects());
        return "assessmentSheet";
    }



    @PostMapping("/save/{projectId}")
    @ResponseBody
    public AssessmentGrade saveAssessment(@PathVariable Long projectId, @RequestBody AssessmentGrade assessment,Model model) {
        Project project=assessment.getProject();
        model.addAttribute("project", project);
        model.addAttribute("assessments", assessmentService.getAllAssessmentsWithProjects());
        model.addAttribute("projects", projectService.getAllProjects());

        return assessmentService.saveAssessment(projectId, assessment);
    }
    }


