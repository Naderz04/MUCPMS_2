package com.MUCPMS.MUCPMS.service;


import com.MUCPMS.MUCPMS.DTO.request.CreateProjectDTO;
import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

//    // Create a new project and save it to the database
//    public Project CreateProject(CreateProjectDTO newProject) {
//        Instructor instructor=i
//        Project project = new Project(newProject.getProjectIdea(), newProject.getProjectDescription(), newProject.getInstructor());
//        return projectRepository.save(project); // Save to database
//    }

    // Delete a project from the database by project ID
    public void DeleteProject(Long projectId) {
        projectRepository.deleteById(projectId); // Deletes using the primary key
    }

    // Retrieve all projects from the database
    public List<Project> getAllProjects() {
        return projectRepository.findAll(); // Fetches all records
    }

    // Retrieve a specific project by ID from the database
    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElse(null); // Fetch by primary key
    }

    public void DeleteAllProjects(){
        projectRepository.deleteAll();
    }
    public Project findById(Long id){
       return projectRepository.findById(id).orElse(null);

    }
}



