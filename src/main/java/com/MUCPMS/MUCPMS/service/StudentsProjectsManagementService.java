package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.model.*;
import com.MUCPMS.MUCPMS.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StudentsProjectsManagementService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectIdeaRepository projectIdeaRepository;
    @Autowired
    private TaskSubmissionRepository taskSubmissionRepository;

    private static final Logger logger = LoggerFactory.getLogger(StudentsProjectsManagementService.class);

    public Student updateStudent(String studentEmail, UpdateStudentDTO updateDTO) {
        Student student = studentRepository.findById(studentEmail).orElse(null);
        if (student == null) {
            // throw new EntityNotFoundException("Student not found");
            return null;
        }

        // Update basic info
        student.setStudentName(updateDTO.getStudentName());
        student.setStudentEmail(updateDTO.getStudentEmail());

        // Handle project change
        if (updateDTO.getProjectId() != null) {
            if (student.getProject() == null || !updateDTO.getProjectId().equals(student.getProject().getProjectId())) {
                if (student.getProject() != null) {
                    Project oldProject = projectRepository.findById(student.getProject().getProjectId()).orElse(null);
                    if (oldProject != null) oldProject.getStudents().remove(student);
                }
                Project newProject = projectRepository.findById(updateDTO.getProjectId()).orElse(null);
                if (newProject != null) {
                    newProject.getStudents().add(student);
                    student.setProject(newProject);
                }
            }
        }

        return studentRepository.save(student); // Save changes
    }


    public void deleteStudent(String studentEmail) {
        Student student = studentRepository.findById(studentEmail).orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Project project = student.getProject();

        if (project != null) {
            project.getStudents().remove(student);  // Remove from Project
        }

        studentRepository.delete(student);  // Now delete the Student
    }

    public Instructor updateInstructor(String email, UpdateInstructorDTO updateDTO) {
        // Find the instructor by email
        Instructor instructor = instructorRepository.findById(email).orElse(null);
        if (instructor == null) {
            // Return null or throw an exception if the instructor is not found
            return null;
        }

        // Update the instructor's name
        instructor.setInstructorName(updateDTO.getInstructorName());

        // Update the projects assigned to this instructor
        if (updateDTO.getProjectsId() != null) {
            // Unassign the instructor from their current projects
            for (Project project : instructor.getProjects()) {
                project.setInstructor(null);
                projectRepository.save(project); // Save the changes
            }
            instructor.getProjects().clear();

            // Assign new projects to the instructor
            for (Long projectId : updateDTO.getProjectsId()) {
                Project project = projectRepository.findById(projectId).orElse(null);
                if (project != null) {
                    // Assign the instructor to the project
                    project.setInstructor(instructor);
                    instructor.getProjects().add(project);
                    projectRepository.save(project); // Save the changes to the project
                }
            }
        }

        // Save and return the updated instructor
        return instructorRepository.save(instructor);
    }

    private void addProjectToInstructor(Instructor instructor, Project project) {
        instructor.getProjects().add(project);
        project.setInstructor(instructor);

        projectRepository.save(project);
        instructorRepository.save(instructor);
    }


    private void removeProjectFromInstructor(Instructor instructor, Project project) {
        instructor.getProjects().remove(project);
        project.setInstructor(null);

        projectRepository.save(project);
        instructorRepository.save(instructor);
    }

    public void addStudentToExistingProject(String studentEmail, Long projectId) {
        Student student = studentRepository.findById(studentEmail).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        if (student != null && project != null) {
            project.getStudents().add(student);
            student.setProject(project);

            projectRepository.save(project);
            studentRepository.save(student);
        }
    }


    private void removeProjectFromStudent(Student student, Project project) {

        if (student != null && project != null) {
            student.setProject(null);
            project.getStudents().remove(student);

            projectRepository.save(project);
            studentRepository.save(student);
        }
    }
    // Create a new project and save it to the database

    public Project createProject(String projectName, String projectDescription, String instructorEmail, String studentEmail) {
        Student student = studentRepository.findById(studentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Instructor instructor = instructorRepository.findById(instructorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found"));

        Project project = new Project();
        project.setProjectIdea(projectName);
        project.setProjectDescription(projectDescription);
        project.setInstructor(instructor);
        project.getStudents().add(student);

        student.setProject(project);

        return projectRepository.save(project);
    }

    public void sendJoinRequest(Long projectId, String studentEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found"));
        Student student = studentRepository.findById(studentEmail)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (project.getStudents().size() >= 2) {
            throw new IllegalStateException("Project already has maximum number of students");
        }

        // In a real application, you would create a JoinRequest entity and save it
        // For simplicity, we're directly adding the student to the project
        project.getStudents().add(student);
        student.setProject(project);

        projectRepository.save(project);
        studentRepository.save(student);
    }



    public Project updateProject(Long projectId, UpdateProjectDTO updateDTO) {
        // Find the project by ID
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            // Return null or throw an exception if the project is not found
            return null;
        }

        // Update project details
        project.setProjectIdea(updateDTO.getProjectIdea());
        project.setProjectDescription(updateDTO.getProjectDescription());

        // Update students
        if (updateDTO.getStudentsEmails() != null) {
            // Remove existing students from the project
            for (Student student : project.getStudents()) {
                student.setProject(null);
                studentRepository.save(student);
            }
            project.getStudents().clear();

            // Add new students to the project
            for (String studentEmail : updateDTO.getStudentsEmails()) {
                Student student = studentRepository.findById(studentEmail).orElse(null);
                if (student != null) {
                    project.getStudents().add(student);
                    student.setProject(project);
                    studentRepository.save(student);
                }
            }
        }

        // Update the instructor
        if (updateDTO.getInstructorEmail() != null) {
            // Unassign the current instructor from this project, if any
            if (project.getInstructor() != null) {
                Instructor currentInstructor = project.getInstructor();
                currentInstructor.getProjects().remove(project);
                instructorRepository.save(currentInstructor);
            }

            // Assign the new instructor to the project
            Instructor newInstructor = instructorRepository.findById(updateDTO.getInstructorEmail()).orElse(null);
            if (newInstructor != null) {
                project.setInstructor(newInstructor);
                newInstructor.getProjects().add(project);
                instructorRepository.save(newInstructor);
            }
        }

        // Save and return the updated project
        return projectRepository.save(project);
    }

    public List<Project> getProjectsByInstructorEmail(String instructorEmail) {
        Instructor instructor = instructorRepository.findById(instructorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Instructor not found."));

        return projectRepository.findProjectsByInstructor(instructor);
    }

    public void SelectProjectInstructor(Long projectId, String instructorEmail) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            throw new EntityNotFoundException("Project not found");
        }
        Instructor instructor = instructorRepository.findById(instructorEmail).orElse(null);
        if (instructor == null) {
            throw new EntityNotFoundException("instructor not found");

        }

        project.setInstructor(instructor);
        projectRepository.save(project);
    }


    @Transactional
    public CreateTaskDTO createAndAssignTask(CreateTaskDTO createTaskDTO) {
        try {
            Instructor instructor = instructorRepository.findById(createTaskDTO.getInstructorEmail())
                    .orElseThrow(() -> new EntityNotFoundException("Instructor not found."));

            List<Project> instructorProjects = instructor.getProjects();


            Task task = new Task();
            task.setTaskName(createTaskDTO.getTaskName());
            task.setTaskDescription(createTaskDTO.getTaskDescription());
            task.setDueDate(createTaskDTO.getDueDate());
            task.setAssignedBy(instructor);

            String filePath = null;
            if (createTaskDTO.getFile() != null && !createTaskDTO.getFile().isEmpty()) {
                logger.info("Attempting to save file: {}", createTaskDTO.getFile().getOriginalFilename());
                filePath = saveFile(createTaskDTO.getFile());

                task.setAttachedFilePath(filePath);


                logger.info("File saved path: {}", filePath);
                if (filePath == null) {
                    throw new RuntimeException("Failed to save the attached file.");
                }
            } else {
                logger.info("No file to save or file is empty");
            }

            for (Project project : instructorProjects) {
                project.getTasks().add(task);
                task.getProjects().add(project);
            }

            Task savedTask = taskRepository.save(task);
            projectRepository.saveAll(instructorProjects);

            CreateTaskDTO taskDTO = new CreateTaskDTO();
            taskDTO.setTaskId(savedTask.getTaskId());
            taskDTO.setTaskName(savedTask.getTaskName());
            taskDTO.setTaskDescription(savedTask.getTaskDescription());
            taskDTO.setDueDate(savedTask.getDueDate());
            taskDTO.setInstructorEmail(instructor.getInstructorEmail());
            taskDTO.setAttachedFilePath(savedTask.getAttachedFilePath());

            return taskDTO;
        } catch (Exception e) {
            logger.error("Error creating task: {}", e.getMessage(), e);
            throw new RuntimeException("Error creating task: " + e.getMessage(), e);
        }
    }

    public String saveFile(MultipartFile file) {
        try {
            String uploadDir = "C:/uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the original file
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;
            file.transferTo(new File(filePath));

            // Generate and save the thumbnail
            String thumbnailDir = "C:/uploads/thumbnails/";
            File thumbnailDirectory = new File(thumbnailDir);
            if (!thumbnailDirectory.exists()) {
                thumbnailDirectory.mkdirs();
            }

            String thumbnailPath = thumbnailDir + fileName + "_thumbnail.png";
            if (fileName.endsWith(".pdf")) {
                ThumbnailGenerator.generateThumbnail(filePath, thumbnailPath, 200); // Generate thumbnail for PDF
            } else if (fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                // Resize image to create thumbnail
                BufferedImage image = ImageIO.read(new File(filePath));
                BufferedImage resizedImage = ThumbnailGenerator.resizeImage(image, 200);
                ImageIO.write(resizedImage, "png", new File(thumbnailPath));
            }

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
        }

    }

    public List<Task> getTasksByInstructorEmail(String instructorEmail) {
        Instructor instructor = instructorRepository.findByInstructorEmail(instructorEmail);
        return taskRepository.findByAssignedBy(instructor);
    }


    public ProjectIdea UpdateProjectIdea(Long projectIdeaId, UpdateProjectIdeaDTO updateProjectIdeaDTO){

        ProjectIdea projectIdea = projectIdeaRepository.findById(projectIdeaId).orElse(null);
        if (projectIdea == null) {
//             throw new EntityNotFoundException("Project not found");
            return null;
        }

        projectIdea.setProjectTitle(updateProjectIdeaDTO.getProjectTitle());
        projectIdea.setType(updateProjectIdeaDTO.getType());
        projectIdea.setProjectDescription(updateProjectIdeaDTO.getProjectDescription());
        projectIdea.setAvailability(updateProjectIdeaDTO.getAvailability());
        return projectIdeaRepository.save(projectIdea);

    }


    // Create a new project and save it to the database
    public ProjectIdea createProjectIdea(CreateProjectIdeaDTO createProjectIdeaDTO) {
        // Find the instructor by email
        Instructor instructor = instructorRepository.findById(createProjectIdeaDTO.getSuggestedByEmail())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        // Create a new ProjectIdea
        ProjectIdea projectIdea = new ProjectIdea(
                createProjectIdeaDTO.getProjectTitle(),
                createProjectIdeaDTO.getType(),
                createProjectIdeaDTO.getProjectDescription(),
                createProjectIdeaDTO.getAvailability(),
                instructor
        );

        // Save and return the ProjectIdea
        return projectIdeaRepository.save(projectIdea);
    }




    public ViewProjectDTO getProjectDetails(Long projectId) {
        // Fetch project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        // Map project to ViewProjectDTO
        ViewProjectDTO dto = new ViewProjectDTO();
        dto.setProjectId(project.getProjectId());
        dto.setProjectIdea(project.getProjectIdea());
        dto.setProjectDescription(project.getProjectDescription());
        dto.setStudentsEmails(project.getStudents().stream()
                .map(Student::getStudentEmail)
                .toList());

        // Add task IDs
        dto.setTasksids(project.getTasks().stream()
                .map(Task::getTaskId)
                .toList());

        return dto;
    }

    public ViewTaskSubmissionDTO getTaskDetails(Long projectId, Long taskId) {
        // Retrieve the task by its ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        // Find the specific task submission for the given project and task
        TaskSubmission taskSubmission = taskSubmissionRepository.findByTask_TaskIdAndProject_ProjectId(taskId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Task submission not found for the given project and task"));

        // Build and return the DTO
        return new ViewTaskSubmissionDTO(
                task.getTaskName(),
                taskSubmission.getProject().getProjectIdea(),
                taskSubmission.getSubmissionFilePath(),
                taskSubmission.getGrade(),taskSubmission.getSubmissionDate()
        );
    }




    public List<ViewTaskSubmissionDTO> getTaskSubmissionsForProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));

        List<Task> tasks = project.getTasks();

        List<ViewTaskSubmissionDTO> taskSubmissions = new ArrayList<>();

        for (Task task : tasks) {
            for (TaskSubmission submission : task.getSubmissions()) {
                if (submission.getProject().getProjectId().equals(projectId)) {
                    ViewTaskSubmissionDTO submissionDTO = new ViewTaskSubmissionDTO(submission.getTask().getTaskName(), submission.getProject().getProjectIdea(), submission.getSubmissionFilePath(), submission.getGrade(), submission.getSubmissionDate());
                    submissionDTO.setTaskName(task.getTaskName());
                    submissionDTO.setProjectName(project.getProjectIdea());
                    submissionDTO.setSubmissionFilePath(submission.getSubmissionFilePath());
                    submissionDTO.setGrade(submission.getGrade());
                    submissionDTO.setSubmissionDate(submission.getSubmissionDate());
                    taskSubmissions.add(submissionDTO);
                }
            }
        }

        return taskSubmissions;
    }

    public String getStatusForProject(Task task, Project project) {
        // Check if the project has submitted the task
        boolean isSubmitted = task.getSubmissions().stream()
                .anyMatch(submission -> submission.getProject().equals(project));

        if (isSubmitted) {
            return "Done"; // Task has been submitted by the project
        } else if (task.getDueDate().isBefore(LocalDate.now())) {
            return "Missing"; // Due date has passed, and no submission
        } else {
            return "Assigned"; // Due date is in the future, and no submission
        }
    }
}











