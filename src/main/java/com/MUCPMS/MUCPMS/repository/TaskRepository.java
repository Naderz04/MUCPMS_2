package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByAssignedBy(Instructor instructor);
//    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.submission WHERE t.project.projectId = :projectId")
//    List<Task> findTasksByProjectId(@Param("projectId") Long projectId);

}
