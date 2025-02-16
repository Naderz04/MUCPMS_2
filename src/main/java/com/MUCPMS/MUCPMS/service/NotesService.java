package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.model.Notes;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.repository.NotesRepository;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public Notes addNoteToProject(Long projectId, Notes note) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        note.setProject(project);
        return notesRepository.save(note);
    }
    public List<Notes> getNotesByProjectId(Long projectId) {
        return notesRepository.findByProject_ProjectId(projectId);
    }
    public void deleteNote(Long noteId) {
        notesRepository.deleteById(noteId);
    }
}