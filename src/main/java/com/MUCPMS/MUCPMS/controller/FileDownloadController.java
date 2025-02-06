package com.MUCPMS.MUCPMS.controller;

import com.MUCPMS.MUCPMS.service.StudentsProjectsManagementService;
import com.MUCPMS.MUCPMS.service.ThumbnailService; // Import the thumbnail service if needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class FileDownloadController {

    private final Path fileStorageLocation = Paths.get("C:/uploads/").toAbsolutePath().normalize();

    // You can remove this if you don't need a separate ThumbnailService
    @Autowired
    private ThumbnailService thumbnailService;

    @Autowired
    private StudentsProjectsManagementService studentsProjectsManagementService; // Use existing file storage service


        @GetMapping("/download/{fileName:.+}")
        public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
            try {
                // Decode the file name
                String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());

                // Load file as Resource
                Path filePath = this.fileStorageLocation.resolve(decodedFileName).normalize();
                Resource resource = new UrlResource(filePath.toUri());

                if (resource.exists()) {
                    // Determine the MIME type of the file
                    String mimeType = determineMimeType(decodedFileName);

                    // Set Content-Disposition to "inline" to open the file in the browser
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"");

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(mimeType)) // Set the MIME type
                            .headers(headers)
                            .body(resource);
                } else {
                    throw new RuntimeException("File not found: " + decodedFileName);
                }
            } catch (MalformedURLException | UnsupportedEncodingException ex) {
                throw new RuntimeException("File not found: " + fileName, ex);
            }
        }

// Helper method to determine the MIME type based on the file extension
        private String determineMimeType(String fileName) {
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            switch (extension) {
                case "pdf":
                    return "application/pdf";
                case "jpg":
                case "jpeg":
                    return "image/jpeg";
                case "png":
                    return "image/png";
                case "doc":
                    return "application/msword";
                case "docx":
                    return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                default:
                    return "application/octet-stream"; // Default MIME type for unknown files
            }
        }


    @GetMapping("/download-attachment/{fileName:.+}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String fileName) {
        try {
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());
            Path filePath = this.fileStorageLocation.resolve(decodedFileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("File not found: " + decodedFileName);
            }
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }

    @GetMapping("/download/thumbnails/{fileName:.+}")
    public ResponseEntity<Resource> downloadThumbnail(@PathVariable String fileName) {
        try {
            // Decode the file name
            String decodedFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8.toString());

            // Load thumbnail as Resource
            Path thumbnailPath = Paths.get("C:/uploads/thumbnails/").resolve(decodedFileName).normalize();
            Resource resource = new UrlResource(thumbnailPath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG) // Thumbnails are saved as PNG
                        .body(resource);
            } else {
                throw new RuntimeException("Thumbnail not found: " + decodedFileName);
            }
        } catch (MalformedURLException | UnsupportedEncodingException ex) {
            throw new RuntimeException("Thumbnail not found: " + fileName, ex);
        }
    }

    @PostMapping("/upload")
    public String handleFileUpload(MultipartFile file) {
        // Use the existing saveFile method from your service
        String filePath = studentsProjectsManagementService.saveFile(file); // This is where your saveFile method is called

        // Handle any additional processing if necessary
        // Redirect or return appropriate response
        return "redirect:/success"; // Change this to your success page
    }
}