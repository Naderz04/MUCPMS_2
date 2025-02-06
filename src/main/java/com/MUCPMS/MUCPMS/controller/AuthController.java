package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.model.User;
import com.MUCPMS.MUCPMS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        userService.registerNewUser(user);
        if(user.getRole()== User.Role.INSTRUCTOR)
        return "instructorHome";
        else
            return "studentHome";
    }
//
//    @PostMapping("/login")
//    public String loginUser(@RequestParam String email, @RequestParam String password) {
//        User user = userService.authenticateUser(email, password);
//        if (user == null) {
//            return "login"; // Redirect back to login if authentication fails
//        }
//        if (user.getRole() == User.Role.INSTRUCTOR) {
//            return "instructorHome";
//        } else {
//            return "studentHome";
//        }
//    }

}