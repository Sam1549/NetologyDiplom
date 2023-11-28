package com.example.netologydiplom.controllers;

import com.example.netologydiplom.dto.request.FileEditNameRequest;
import com.example.netologydiplom.dto.response.FileWebResponse;
import com.example.netologydiplom.services.FilesService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@AllArgsConstructor
public class FilesControllers {

    private final FilesService filesService;


    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, MultipartFile file) {
        filesService.uploadFile(authToken, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping("/list")
    public List<FileWebResponse> getAllFiles(@RequestHeader("auth-token") String authToken, @RequestParam Integer limit) {
        return filesService.getAllFiles(authToken);
    }

    @PutMapping("/file")
    public ResponseEntity<?> editFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename, @RequestBody FileEditNameRequest fileEditNameRequest) {
        return filesService.editFile(authToken, filename, fileEditNameRequest);
    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        return filesService.deleteFile(authToken, filename);
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestHeader("auth-token") String authToken, @RequestParam("filename") String filename) {
        return filesService.downloadFile(authToken, filename);

    }
}
