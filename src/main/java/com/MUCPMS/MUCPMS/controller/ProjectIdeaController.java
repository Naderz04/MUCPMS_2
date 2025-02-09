package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.DTO.request.*;
import com.MUCPMS.MUCPMS.DTO.response.ApiResponse;
import com.MUCPMS.MUCPMS.model.*;
import com.MUCPMS.MUCPMS.service.InstructorService;
import com.MUCPMS.MUCPMS.service.ProjectIdeaService;
import com.MUCPMS.MUCPMS.service.ProjectService;
import com.MUCPMS.MUCPMS.service.StudentsProjectsManagementService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value= "/projectIdeas")
public class ProjectIdeaController {

    @Autowired
    private ProjectIdeaService projectIdeaService;
    @Autowired
    private StudentsProjectsManagementService studentsProjectsManagementService;

    @Autowired
    private InstructorService instructorService;

//    @PostMapping
//    public ResponseEntity<ApiResponse<ViewProjectIdeaDTO>> createProjectIdea(@Valid @RequestBody CreateProjectIdeaDTO projectIdeaDTO) {
//        try {
//            ProjectIdea createdProjectIdea = studentsProjectsManagementService.createProjectIdea(projectIdeaDTO);
//            ViewProjectIdeaDTO viewProjectIdeaDTO=new ViewProjectIdeaDTO(projectIdeaDTO.getProjectTitle(),projectIdeaDTO.getProjectDescription(),projectIdeaDTO.getType(),projectIdeaDTO.getType(),projectIdeaDTO.getSuggestedByEmail());
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(new ApiResponse<>(true, viewProjectIdeaDTO, "Project Idea created successfully", HttpStatus.CREATED.value()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to create Project idea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResponse<UpdateProjectIdeaDTO>> updateProjectIdea(@PathVariable Long id, @RequestBody UpdateProjectIdeaDTO updatedProjectIdea) {
//        try {
//            ProjectIdea projectIdea = studentsProjectsManagementService.UpdateProjectIdea(id, updatedProjectIdea);
//            if (projectIdea != null) {
//                return ResponseEntity.ok(new ApiResponse<>(true, updatedProjectIdea, "Project idea updated successfully", HttpStatus.OK.value()));
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                        .body(new ApiResponse<>(false, null, "Project idea not found", HttpStatus.NOT_FOUND.value()));
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to update project idea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }
//
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<Void>> deleteProjectIdea(@PathVariable Long id) {
//        try {
//            projectIdeaService.DeleteProjectIdea(id);
//            return ResponseEntity.ok(new ApiResponse<>(true, null, "Project idea deleted successfully", HttpStatus.OK.value()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to delete Project idea: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }
//
//    @DeleteMapping("/deleteAll")
//    public ResponseEntity<ApiResponse<Void>> deleteAllProjectIdeas() {
//        try {
//            projectIdeaService.DeleteAllProjects();
//            return ResponseEntity.status(HttpStatus.OK)
//                    .body(new ApiResponse<>(true, null, "All project ideas deleted successfully", HttpStatus.OK.value()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new ApiResponse<>(false, null, "Failed to delete all project ideas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
//        }
//    }
//
//    @GetMapping
//    public String getAllProjectIdeasPage(Model model) {
//        // Fetch all project ideas
//        List<ProjectIdea> projectIdeas = projectIdeaService.getAllProjectsIdeas();
//
//        // Map each ProjectIdea to a DTO and add it to the model
//        List<ViewProjectIdeaDTO> viewProjectIdeaDTOS = projectIdeas.stream().map(projectIdea -> {
//            String suggestedBy = projectIdea.getSuggestedBy() != null
//                    ? projectIdea.getSuggestedBy().getInstructorEmail()
//                    : null;
//
//            return new ViewProjectIdeaDTO(
//                    projectIdea.getProjectTitle(),
//                    projectIdea.getType(),
//                    projectIdea.getProjectDescription(),
//                    projectIdea.getAvailability(),
//                    suggestedBy
//            );
//        }).collect(Collectors.toList());
//
//        model.addAttribute("projectIdeas", viewProjectIdeaDTOS);
//
//        // Return the Thymeleaf HTML page name (without .html)
//        return "projectIdeas";
//    }

    @PostMapping
    public String createProjectIdea(@Valid @ModelAttribute CreateProjectIdeaDTO projectIdeaDTO, Model model) {
        try {
            ProjectIdea createdProjectIdea = studentsProjectsManagementService.createProjectIdea(projectIdeaDTO);
            ViewProjectIdeaDTO viewProjectIdeaDTO = new ViewProjectIdeaDTO(
                    projectIdeaDTO.getProjectTitle(),
                    projectIdeaDTO.getProjectDescription(),
                    projectIdeaDTO.getType(),
                    projectIdeaDTO.getType(),
                    projectIdeaDTO.getSuggestedByEmail()
            );
            model.addAttribute("successMessage", "Project Idea created successfully");
            model.addAttribute("projectIdea", viewProjectIdeaDTO);
            return "projectIdeaDetails"; // Returns a view (e.g., a Thymeleaf or JSP page)
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to create Project idea: " + e.getMessage());
            return "errorPage"; // Replace with the appropriate error view name
        }
    }

    @PutMapping("/{id}")
    public String updateProjectIdea(@PathVariable Long id, @ModelAttribute UpdateProjectIdeaDTO updatedProjectIdea, Model model) {
        try {
            ProjectIdea projectIdea = studentsProjectsManagementService.UpdateProjectIdea(id, updatedProjectIdea);
            if (projectIdea != null) {
                model.addAttribute("successMessage", "Project idea updated successfully");
                model.addAttribute("projectIdea", updatedProjectIdea);
                return "projectIdeaDetails"; // Redirect to or render the updated project idea details page
            } else {
                model.addAttribute("errorMessage", "Project idea not found");
                return "notFoundPage"; // Replace with the appropriate not-found view
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update project idea: " + e.getMessage());
            return "errorPage"; // Replace with the appropriate error view name
        }
    }



    @DeleteMapping("/deleteAll")
    public String deleteAllProjectIdeas(RedirectAttributes redirectAttributes) {
        try {
            projectIdeaService.DeleteAllProjects();
            redirectAttributes.addFlashAttribute("successMessage", "All project ideas deleted successfully");
            return "redirect:/project-ideas"; // Redirect to the project ideas list page
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete all project ideas: " + e.getMessage());
            return "redirect:/error"; // Redirect to a generic error page
        }
    }


    @GetMapping()
    public String getProjectIdeas(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        List<ProjectIdea> projectIdeas = projectIdeaService.getAllProjectsIdeas();
        model.addAttribute("projectIdeas", projectIdeas);
        // Return the name of the Thymeleaf HTML page (e.g., projectIdeas.html)
        return "projectIdeas";
    }

    @GetMapping("/instructorHome")
    public String instructorHome(Model model, @RequestParam String instructorEmail) {
        List<Task> tasks = studentsProjectsManagementService.getTasksByInstructorEmail(instructorEmail);
        List<Project> projects = studentsProjectsManagementService.getProjectsByInstructorEmail(instructorEmail);
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projects);
        return "instructorHome";
    }



    // Display form to create a new project idea
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("projectIdea", new ProjectIdea());
        model.addAttribute("instructors", instructorService.getAllInstructors()); // For dropdown in form
        return "CreateProjectIdea"; // Thymeleaf template for the create form
    }

    // Handle form submission to create a new project idea
    @PostMapping("/new")
    public String createProjectIdea(@ModelAttribute ProjectIdea projectIdea) {

        projectIdeaService.saveProjectIdea(projectIdea);
        return "redirect:/projectIdeas"; // Redirect to the list of project ideas
    }

    // Display form to edit an existing project idea
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProjectIdea projectIdea = projectIdeaService.getProjectIdeaById(id);
        model.addAttribute("projectIdea", projectIdea);
        model.addAttribute("instructors", instructorService.getAllInstructors()); // For dropdown in form
        return "EditProjectIdea"; // Thymeleaf template for the edit form
    }

    // Handle form submission to update an existing project idea
    @PostMapping("/edit/{id}")
    public String updateProjectIdea(@PathVariable Long id, @ModelAttribute ProjectIdea projectIdea, @RequestParam String instructorEmail) {
        Instructor instructor = instructorService.getInstructorByEmail(instructorEmail);
        projectIdea.setSuggestedBy(instructor);
        projectIdeaService.saveProjectIdea(projectIdea);
        return "redirect:/projectIdeas"; // Redirect to the list of project ideas
    }

    // Handle deletion of a project idea
    @GetMapping("/delete/{id}")
    public String deleteProjectIdea(@PathVariable Long id) {
        projectIdeaService.DeleteProjectIdeaById(id);
        return "redirect:/projectIdeas"; // Redirect to the list of project ideas
    }

}
