package com.example.learningcenterapi.bucket;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/files")
public class BucketController {

    private final BucketService googleCloudStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        googleCloudStorageService.uploadFile(file.getOriginalFilename(), file.getBytes());
        return ResponseEntity.ok("File uploaded successfully");
    }

    @GetMapping("/generate-link")
    public ResponseEntity<String> generateLink(@RequestParam("fileName") String fileName) {
        String url = googleCloudStorageService.generateTemporaryLinkForReadingFile(fileName);
        return ResponseEntity.ok(url);
    }

    @GetMapping("/test")
    public String testConnection() {
        return "API works. Time for upload ;)";
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("fileName") String fileName) {
        googleCloudStorageService.deleteFile(fileName);
        return ResponseEntity.ok("File deleted successfully");
    }
}
