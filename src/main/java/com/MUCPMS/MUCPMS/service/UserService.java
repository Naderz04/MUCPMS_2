package com.MUCPMS.MUCPMS.service;

import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.Student;
import com.MUCPMS.MUCPMS.model.User;
import com.MUCPMS.MUCPMS.repository.InstructorRepository;
import com.MUCPMS.MUCPMS.repository.StudentRepository;
import com.MUCPMS.MUCPMS.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public void registerNewUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        if (user.getRole() == User.Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setStudentName(user.getName());
            studentRepository.save(student);
        } else if (user.getRole() == User.Role.INSTRUCTOR) {
            Instructor instructor = new Instructor();
            instructor.setUser(savedUser);
            instructor.setInstructorName(user.getName());
            instructorRepository.save(instructor);
        }
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) { // Ideally, use password hashing
            return user;
        }
        return null;
    }

}