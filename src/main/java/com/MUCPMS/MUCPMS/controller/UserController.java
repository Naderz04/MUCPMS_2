package com.MUCPMS.MUCPMS.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class UserController {

    @GetMapping("/")
    public String showHomePage() {
        return "home"; // Load index.html as the first page
    }
}
