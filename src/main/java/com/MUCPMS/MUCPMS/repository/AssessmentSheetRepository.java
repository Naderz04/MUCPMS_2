package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.AssessmentGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentSheetRepository extends JpaRepository<AssessmentGrade, Long> {
   Optional <AssessmentGrade> findByProject_ProjectId(Long projectId);
}