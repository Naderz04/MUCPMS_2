package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.model.Instructor;
import com.MUCPMS.MUCPMS.model.User;
import com.MUCPMS.MUCPMS.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;
import java.nio.file.Paths;
@RequestMapping("/users")
@Controller
public class UserController {

    private UserService userService;
    @GetMapping("/")
    public String showHomePage() {
        return "home"; // Load index.html as the first page
    }

    @GetMapping("/{email}/photo")
    public ResponseEntity<Resource> getUserPhoto(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user == null || user.getPhotoPath() == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            Path path = Paths.get("uploads/" + user.getPhotoPath());
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
