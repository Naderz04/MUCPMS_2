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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       StudentRepository studentRepository,
                       InstructorRepository instructorRepository,
                       PasswordEncoder passwordEncoder,
                       FileUploadService fileUploadService) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileUploadService = fileUploadService;
    }

    @Transactional
    public void registerNewUser(User user, MultipartFile photo) throws IOException {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (photo != null && !photo.isEmpty()) {
            String photoPath = fileUploadService.uploadFile(photo, "users");
            user.setPhotoPath(photoPath);
        }

        User savedUser = userRepository.save(user);

        if (user.getRole() == User.Role.STUDENT) {
            Student student = new Student();
            student.setUser(savedUser);
            student.setStudentName(user.getName());
            student.setStudentPhoto(user.getPhotoPath());
            studentRepository.save(student);
        } else if (user.getRole() == User.Role.INSTRUCTOR) {
            Instructor instructor = new Instructor();
            instructor.setUser(savedUser);
            instructor.setInstructorName(user.getName());
            instructor.setInstructorPhoto(user.getPhotoPath());
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