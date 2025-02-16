package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.model.AssessmentGrade;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.repository.AssessmentSheetRepository;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssessmentService {
    @Autowired
    private AssessmentSheetRepository assessmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<AssessmentGrade> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Optional<AssessmentGrade> getAssessmentByProjectId(Long projectId) {
        return assessmentRepository.findByProject_ProjectId(projectId);
    }




    @Transactional
    public List<AssessmentGrade> getAllAssessmentsWithProjects() {
        // Get all projects
        List<Project> allProjects = projectRepository.findAll();

        // Get existing assessments
        List<AssessmentGrade> existingAssessments = assessmentRepository.findAll();

        // Create a map of project IDs to assessments
        Map<Long, AssessmentGrade> projectAssessments = existingAssessments.stream()
                .collect(Collectors.toMap(a -> a.getProject().getProjectId(), a -> a));

        // Create or get assessment for each project
        return allProjects.stream()
                .map(project -> projectAssessments.computeIfAbsent(
                        project.getProjectId(),
                        k -> assessmentRepository.save(new AssessmentGrade(project))
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public AssessmentGrade saveAssessment(Long projectId, AssessmentGrade assessmentData) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        AssessmentGrade assessment = assessmentRepository.findByProject_ProjectId(projectId)
                .orElse(new AssessmentGrade(project));

        // Update assessment data
        assessment.setReportSupervisor(assessmentData.getReportSupervisor());
        assessment.setCsc498Supervisor(assessmentData.getCsc498Supervisor());
        assessment.setPresentationSupervisor(assessmentData.getPresentationSupervisor());
        assessment.setPresentationCommittee1(assessmentData.getPresentationCommittee1());
        assessment.setPresentationCommittee2(assessmentData.getPresentationCommittee2());
        assessment.setImplementationSupervisor(assessmentData.getImplementationSupervisor());
        assessment.setImplementationCommittee1(assessmentData.getImplementationCommittee1());
        assessment.setImplementationCommittee2(assessmentData.getImplementationCommittee2());
        assessment.setExtraSupervisor(assessmentData.getExtraSupervisor());
        assessment.setExtraCommittee1(assessmentData.getExtraCommittee1());
        assessment.setExtraCommittee2(assessmentData.getExtraCommittee2());

        return assessmentRepository.save(assessment);
    }
}

