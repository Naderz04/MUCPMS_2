package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.CreateStudentDTO;
import com.MUCPMS.MUCPMS.DTO.request.ViewStudentDTO;
import com.MUCPMS.MUCPMS.model.Project;
import com.MUCPMS.MUCPMS.model.Student;
import com.MUCPMS.MUCPMS.repository.ProjectRepository;
import com.MUCPMS.MUCPMS.repository.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }



    // Create a new student and save it to the database
    public Student CreateStudent(CreateStudentDTO newStudent) {
        Student student = new Student(newStudent.getStudentEmail(), newStudent.getStudentName(),newStudent.getStudentPhoto());
        return studentRepository.save(student); // Save to database
    }


    public List<Student> getAllStudents() {
        return studentRepository.findAll(); // Fetches all records
    }

    // Retrieve a specific student by email from the database
    public Student getStudentByEmail(String email) {
        return studentRepository.findById(email).orElse(null); // Fetch by primary key
    }
}
