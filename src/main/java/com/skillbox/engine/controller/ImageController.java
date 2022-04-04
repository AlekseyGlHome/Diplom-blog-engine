package com.skillbox.engine.controller;

import com.skillbox.engine.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ImageController {
    @Value("${config.uploadFile}")
    private String uploadFile;

    private final ImageService imageService;

    @PostMapping("/api/image")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public String uploadFile(@RequestParam("image") MultipartFile file) throws IOException {



        return imageService.loadImage(file);
    }
}
