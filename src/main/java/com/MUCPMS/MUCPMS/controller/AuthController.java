package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.model.User;
import com.MUCPMS.MUCPMS.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String registerUser(@ModelAttribute User user, @RequestParam(value = "photo", required = false) MultipartFile photo, Model model) {
        try {
            userService.registerNewUser(user, photo);
            if (user.getRole() == User.Role.INSTRUCTOR) {
                return "instructorHome";
            } else {
                return "project-creation";
            }
        } catch (IOException e) {
            model.addAttribute("errorMessage", "Failed to upload photo: " + e.getMessage());
            return "register"; // Return to the registration page with an error message
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
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