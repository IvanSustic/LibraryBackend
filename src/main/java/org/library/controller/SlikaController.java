package org.library.controller;

import lombok.RequiredArgsConstructor;
import org.library.service.SlikaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/slika")
@RequiredArgsConstructor
public class SlikaController {

    private final SlikaService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file, "/knjige");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/knjige/")
                .path(fileName)
                .toUriString();

        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("fileDownloadUri", fileDownloadUri);
        response.put("fileType", file.getContentType());
        response.put("fileSize", String.valueOf(file.getSize()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/uploadKnjiznica")
    public ResponseEntity<Map<String, String>> uploadFileKnjiznica(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file, "/knjiznice");

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/knjiznice/")
                .path(fileName)
                .toUriString();

        Map<String, String> response = new HashMap<>();
        response.put("fileName", fileName);
        response.put("fileDownloadUri", fileDownloadUri);
        response.put("fileType", file.getContentType());
        response.put("fileSize", String.valueOf(file.getSize()));

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody String slika) {
        try {
            fileStorageService.deleteFile(slika,"knjige");
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/deleteKnjiznica")
    public ResponseEntity<?> deleteKnjiznica(@RequestBody String slika) {
        try {
            fileStorageService.deleteFile(slika,"knjiznice");
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
