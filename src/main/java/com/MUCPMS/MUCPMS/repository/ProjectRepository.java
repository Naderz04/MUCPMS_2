package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,Long> {
    List<Project> findProjectsByInstructor (Instructor instructor);
    List<Project> findByStatus(String status);

}
