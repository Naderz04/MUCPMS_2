package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.CreateProjectDTO;
import com.MUCPMS.MUCPMS.DTO.request.CreateProjectIdeaDTO;
import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.ProjectIdea;
import com.MUCPMS.MUCPMS.repository.InstructorRepository;
import com.MUCPMS.MUCPMS.repository.ProjectIdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectIdeaService {


    private final ProjectIdeaRepository projectIdeaRepository;

    @Autowired
    public ProjectIdeaService(ProjectIdeaRepository projectIdeaRepository) {
        this.projectIdeaRepository = projectIdeaRepository;
    }


    // Delete a project from the database by project ID
    public void DeleteProjectIdea(Long projectIdeaId) {
        projectIdeaRepository.deleteById(projectIdeaId); // Deletes using the primary key
    }

    // Retrieve all projects from the database
    public List<ProjectIdea> getAllProjectsIdeas() {
        return projectIdeaRepository.findAll(); // Fetches all records
    }

    // Retrieve a specific project by ID from the database
    public ProjectIdea getProjectIdeaById(Long projectIdeaId) {
        return projectIdeaRepository.findById(projectIdeaId).orElse(null); // Fetch by primary key
    }
    public void DeleteProjectIdeaById(Long id){
        projectIdeaRepository.deleteById(id);
    }
    public void DeleteAllProjects(){
        projectIdeaRepository.deleteAll();
    }
    public void saveProjectIdea(ProjectIdea projectIdea) {
        projectIdeaRepository.save(projectIdea);
    }

}
