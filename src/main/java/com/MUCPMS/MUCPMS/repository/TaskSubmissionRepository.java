package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskSubmissionRepository extends JpaRepository<TaskSubmission, Long> {
    List<TaskSubmission> findByTask_TaskId(Long taskId); // Find all submissions for a task.
    List<TaskSubmission> findByProject_ProjectId(Long projectId); // Find all submissions for a project.

        Optional<TaskSubmission> findByTask_TaskIdAndProject_ProjectId(Long taskId, Long projectId);
    }


