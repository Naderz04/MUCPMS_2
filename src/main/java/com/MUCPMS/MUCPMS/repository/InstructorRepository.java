package com.MUCPMS.MUCPMS.repository;

import com.MUCPMS.MUCPMS.model.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstructorRepository extends JpaRepository<Instructor,String> {

    Instructor findByInstructorEmail(String email);

}
