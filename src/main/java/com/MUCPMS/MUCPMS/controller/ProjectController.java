package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Student;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import com.MUCPMS.MUCPMS.repository.TaskRepository;
import com.MUCPMS.MUCPMS.service.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private StudentsProjectsManagementService studentsProjectsManagementService;
    @Autowired
    private TaskSubmissionService taskSubmissionService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private StudentService studentService;

    @Autowired
    private InstructorService instructorService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<ViewProjectDTO>> createProject(@Valid @RequestBody CreateProjectDTO projectDTO) {
//        try {
//            // Attempt to create the project
//            Project createdProject = studentsProjectsManagementService.createProject(projectDTO);
//
//            // Prepare response DTO
//            List<String> studentIds = createdProject.getStudents().stream()
//                    .map(Student::getStudentEmail)
//                    .collect(Collectors.toList());
//
//            String instructorId = createdProject.getInstructor() != null ? createdProject.getInstructor().getInstructorEmail() : null;
//
//            List<Long> taskIds = createdProject.getTasks().stream()
//                    .map(Task::getTaskId)
//                    .collect(Collectors.toList());
//
//            ViewProjectDTO viewProjectDTO = new ViewProjectDTO(
//                    createdProject.getProjectId(),
//                    createdProject.getProjectIdea(),
//                    createdProject.getProjectDescription(),
//                    studentIds,
//                    instructorId,
//                    taskIds
//            );
//
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse<>(true, viewProjectDTO, "Project created successfully", HttpStatus.CREATED.value()));
//        } catch (EntityNotFoundException e) {
//            // Handle missing instructor
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(new ApiResponse<>(false, null, e.getMessage(), HttpStatus.NOT_FOUND.value()));
//        } catch (Exception e) {
//            // Handle other errors
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to create Project: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ViewProjectDTO>>> getAllProjects() {
        try {
            // Fetch all projects from the service layer
            List<Project> projectList = projectService.getAllProjects();

            // Map each Project model to a ViewProjectDTO
            List<ViewProjectDTO> projectDTOs = projectList.stream().map(project -> {
                // Extract the list of student emails (IDs)
                List<String> studentIds = project.getStudents().stream()
                        .map(Student::getStudentEmail) // Assuming email is the ID
                        .collect(Collectors.toList());

                // Get the instructor's email (single instructor)
                String instructorId = project.getInstructor() != null ? project.getInstructor().getInstructorEmail() : null;

                // Extract the list of task IDs
                List<Long> tasksIds = project.getTasks().stream()
                        .map(Task::getTaskId)
                        .collect(Collectors.toList());

                // Create a new ViewProjectDTO for each project
                return new ViewProjectDTO(
                        project.getProjectId(),
                        project.getProjectIdea(),
                        project.getProjectDescription(),
                        studentIds,
                        instructorId, // Single instructor ID
                        tasksIds
                );
            }).collect(Collectors.toList());

            // Return the list of DTOs in an ApiResponse
            return ResponseEntity.ok(new ApiResponse<>(true, projectDTOs, "Projects retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve Projects: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ViewProjectDTO>> getProjectById(@PathVariable Long id) {
        try {
            Project project = projectService.getProjectById(id);

            if (project != null) {
                // Extract the list of student IDs
                List<String> studentIds = project.getStudents().stream()
                        .map(Student::getStudentEmail) // Assuming email is the ID
                        .collect(Collectors.toList());

                // Get the instructor's email (single instructor)
                String instructorId = project.getInstructor() != null ? project.getInstructor().getInstructorEmail() : null;

                // Extract the list of task IDs
                List<Long> tasksIds = project.getTasks().stream()
                        .map(Task::getTaskId)
                        .collect(Collectors.toList());

                // Create the ViewProjectDTO
                ViewProjectDTO viewProjectDTO = new ViewProjectDTO(
                        project.getProjectId(),
                        project.getProjectIdea(),
                        project.getProjectDescription(),
                        studentIds,
                        instructorId, // Single instructor ID

                        tasksIds
                );

                return ResponseEntity.ok(new ApiResponse<>(true, viewProjectDTO, "Project retrieved successfully", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Project not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve Project: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateProjectDTO>> updateProject(@PathVariable Long id, @RequestBody UpdateProjectDTO updatedProject) {
        try {
            Project project = studentsProjectsManagementService.updateProject(id, updatedProject);
            if (project != null) {
                return ResponseEntity.ok(new ApiResponse<>(true, updatedProject, "Project updated successfully", HttpStatus.OK.value()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, null, "Project not found", HttpStatus.NOT_FOUND.value()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to update project: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/projects/{projectId}/students/{studentEmail}")
    public ResponseEntity<ApiResponse<String>> addStudentToProject(
            @PathVariable Long projectId,
            @PathVariable String studentEmail) {
        try {
            // Add the student to the project
            studentsProjectsManagementService.addStudentToExistingProject(studentEmail, projectId);
            return ResponseEntity.ok(new ApiResponse<>(
                    true,
                    null,
                    "Student with email " + studentEmail + " successfully added to project with ID " + projectId,
                    HttpStatus.OK.value()
            ));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    false,
                    null,
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    false,
                    null,
                    "Failed to add student to project: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            ));
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        try {
            projectService.DeleteProject(id);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "Project deleted successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to delete Project: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<ApiResponse<Void>> deleteAllProjects() {
        try {
            projectService.DeleteAllProjects();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>(true, null, "All projects deleted successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to delete all projects: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @PostMapping("/{projectId}/select-instructor/{instructorEmail}")
    public ResponseEntity<ApiResponse<String>> selectProjectInstructor(
            @PathVariable Long projectId,
            @PathVariable String instructorEmail) {
        try {
            studentsProjectsManagementService.SelectProjectInstructor(projectId, instructorEmail);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "Instructor assigned to project successfully", HttpStatus.OK.value()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, null, e.getMessage(), HttpStatus.NOT_FOUND.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to assign instructor to project: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }



    // Endpoint to get project details
    @GetMapping("/{projectId}/details")
    public ResponseEntity<ApiResponse<ViewProjectDTO>> getProjectDetails(@PathVariable Long projectId) {
        try {
            ViewProjectDTO projectDetails = studentsProjectsManagementService.getProjectDetails(projectId);
            return ResponseEntity.ok(new ApiResponse<>(true, projectDetails, "Project details retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve project details: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Endpoint to get all tasks for a project
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<ApiResponse<List<ViewTaskSubmissionDTO>>> getProjectTasks(@PathVariable Long projectId) {
        try {
            List<ViewTaskSubmissionDTO> tasks = studentsProjectsManagementService.getTaskSubmissionsForProject(projectId);
            return ResponseEntity.ok(new ApiResponse<>(true, tasks, "Project tasks retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve project tasks: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Endpoint to get task submissions for a project
    @GetMapping("/projects/{projectId}/task-submissions")
    public ResponseEntity<ApiResponse<List<ViewTaskSubmissionDTO>>> getTaskSubmissions(@PathVariable Long projectId) {
        try {
            List<ViewTaskSubmissionDTO> submissions = studentsProjectsManagementService.getTaskSubmissionsForProject(projectId);
            return ResponseEntity.ok(new ApiResponse<>(true, submissions, "Task submissions retrieved successfully", HttpStatus.OK.value()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, null, "Failed to retrieve task submissions: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    @GetMapping("/studentHome")
    public String getTasksForProject(Model model, Authentication authentication) {
        // Fetch the project
        Student s=studentService.getStudentByEmail(authentication.getName());
        Long projectId=s.getProject().getProjectId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

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

    @GetMapping("/project-creation")
    public String showProjectCreation(Model model) {
        List<Instructor> instructors = instructorService.getAllInstructors();
        List<Project> projects = projectService.getAllProjects();

        System.out.println("Number of instructors: " + instructors.size());
        System.out.println("Number of projects: " + projects.size());

        model.addAttribute("instructors",instructors);
        model.addAttribute("projects",projects);
        return "project-creation";
    }

//    @GetMapping("/instructorTasks")
//    public String getAllTasks(Model model, Authentication authentication) {
//        String email = authentication.getName();
//        Instructor instructor = instructorService.getInstructorByEmail(email);
//        List<Task> taskList = studentsProjectsManagementService.getTasksByInstructorEmail(email);
//        model.addAttribute("tasks", taskList);
//        return "instructorTasks";
//    }


    @PostMapping("/create-project")
    public String createProject(@RequestParam String projectName,
                                @RequestParam String projectDescription,
                                @RequestParam String instructorEmail,
                                @AuthenticationPrincipal UserDetails userDetails) {

        studentsProjectsManagementService.createProject(projectName, projectDescription, instructorEmail,userDetails.getUsername());
        return "redirect:/studentHome";
    }

    @PostMapping("/join-project/{projectId}")
    @ResponseBody
    public Map<String, Object> joinProject(@PathVariable Long projectId,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
//        dme mansooooor,code taba3a b3do ma mtabba2
            studentsProjectsManagementService.sendJoinRequest(projectId, userDetails.getUsername());
            return Map.of("success", true);
        } catch (Exception e) {
            return Map.of("success", false, "message", e.getMessage());
        }
    }
}

