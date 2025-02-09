package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.DTO.request.CreateInstructorDTO;
import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class InstructorService {

    private final InstructorRepository instructorRepository;

    @Autowired
    public InstructorService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    // Create a new instructor and save to the database
    public Instructor CreateInstructor(CreateInstructorDTO newInstructor) {
        Instructor instructor = new Instructor(newInstructor.getInstructorEmail(), newInstructor.getInstructorName(), newInstructor.getInstructorPhoto());
        return instructorRepository.save(instructor); // Save to database
    }

    // Delete an instructor from the database by email
    public void DeleteInstructor(String email) {
        instructorRepository.deleteById(email); // Deletes using the primary key
    }

    // Retrieve all instructors from the database
    public List<Instructor> getAllInstructors() {
        return instructorRepository.findAll(); // Fetches all records
    }

    // Retrieve a specific instructor by email
    public Instructor getInstructorByEmail(String email) {
        return instructorRepository.findById(email).orElse(null); // Fetch by primary key
    }
}
