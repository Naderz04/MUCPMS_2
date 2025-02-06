package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.CreateStudentDTO;
import com.MUCPMS.MUCPMS.DTO.request.CreateTaskDTO;
import com.MUCPMS.MUCPMS.model.Student;
import com.MUCPMS.MUCPMS.model.Task;
import com.MUCPMS.MUCPMS.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskService {

    private final TaskRepository taskRepository;


    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

//
//    // Create a new Task and save it to the database
//    public Task CreateTask(CreateTaskDTO newTask) {
//        Task task = new Task(newTask.getTaskName(), newTask.getTaskDescription(),newTask.getDueDate(),newTask.getProjectIds());
//        return taskRepository.save(task); // Save to database
//    }

    public void deleteAllTasks(){
        taskRepository.deleteAll();
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll(); // Fetches all records
    }

//    // Retrieve a specific student by email from the database
//    public Student getStudentByEmail(String email) {
//        return studentRepository.findById(email).orElse(null); // Fetch by primary key
//    }

}
