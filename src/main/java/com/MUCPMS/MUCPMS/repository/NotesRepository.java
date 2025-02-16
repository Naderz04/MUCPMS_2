package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.Notes;
import com.MUCPMS.MUCPMS.model.TaskSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    List<Notes> findByProject_ProjectId(Long projectId); // Find all notes for a project.




}