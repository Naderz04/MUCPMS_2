package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.CreateTaskSubmissionDTO;
import com.MUCPMS.MUCPMS.DTO.request.UpdateGradeDTO;
import com.MUCPMS.MUCPMS.DTO.request.ViewTaskSubmissionDTO;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.model.TaskSubmission;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import com.MUCPMS.MUCPMS.repository.TaskRepository;
import com.MUCPMS.MUCPMS.repository.TaskSubmissionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

    @Service
    @RequiredArgsConstructor
    public class TaskSubmissionService {

        @Autowired
        private final TaskSubmissionRepository taskSubmissionRepository;
        @Autowired
        private final TaskRepository taskRepository;
        @Autowired
        private final ProjectRepository projectRepository;

        @Transactional
        public TaskSubmission createTaskSubmission(CreateTaskSubmissionDTO dto) {
            try {
                // Fetch task
                Task task = taskRepository.findById(dto.getTaskId())
                        .orElseThrow(() -> new EntityNotFoundException("Task not found."));

                // Fetch project
                Project project = projectRepository.findById(dto.getProjectId())
                        .orElseThrow(() -> new EntityNotFoundException("Project not found."));

                // Save the file
                String filePath = null;
                if (dto.getSubmissionFile() != null && !dto.getSubmissionFile().isEmpty()) {
                    filePath = saveFile(dto.getSubmissionFile());
                }

                // Create and save the task submission
                TaskSubmission submission = new TaskSubmission();
                submission.setTask(task);
                submission.setProject(project);
                submission.setSubmissionFilePath(filePath);
                submission.setSubmissionDate(new Date());
                taskSubmissionRepository.save(submission);

                return submission;
            } catch (Exception e) {
                throw new RuntimeException("Error creating task submission: " + e.getMessage(), e);
            }
        }

        private String saveFile(MultipartFile file) {
            // Implement file saving logic here
            // Example: Save to a directory and return the file path
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String filePath = uploadDir + file.getOriginalFilename();
            try {
                file.transferTo(new File(filePath));
                return filePath;
            } catch (IOException e) {
                throw new RuntimeException("Failed to save file: " + e.getMessage(), e);
            }
        }



        // Update grade for a submission
        public TaskSubmission updateGrade(UpdateGradeDTO dto) {
            TaskSubmission submission = taskSubmissionRepository.findById(dto.getTaskSubmissionId()).orElseThrow(() -> new EntityNotFoundException("Task submission not found"));

            submission.setGrade(dto.getGrade());
            return taskSubmissionRepository.save(submission);
        }

        // View submissions
        public List<ViewTaskSubmissionDTO> getSubmissionsByTask(Long taskId) {
            return taskSubmissionRepository.findByTask_TaskId(taskId).stream()
                    .map(submission -> new ViewTaskSubmissionDTO(
                            submission.getTask().getTaskName(),
                            submission.getProject().getProjectIdea(),
                            submission.getSubmissionFilePath(),
                            submission.getGrade(),
                            submission.getSubmissionDate()
                    ))
                    .collect(Collectors.toList());
        }



    }


