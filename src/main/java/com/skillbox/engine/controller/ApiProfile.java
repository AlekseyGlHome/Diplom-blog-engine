package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.Profile;
import com.skillbox.engine.api.response.UpdateUserResponse;
import com.skillbox.engine.exception.LoadImageExceprion;
import com.skillbox.engine.service.ImageService;
import com.skillbox.engine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ApiProfile {

    private final UserService userService;
    private final ImageService imageService;

    @PostMapping(value = "/my", consumes = "application/json")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<UpdateUserResponse> MyWithoutPhoto(
            Principal principal,
            @RequestBody(required = false) Profile profile) {

        return ResponseEntity.ok(userService.updateUser(principal.getName(), profile));
    }

    @PostMapping(value = "/my", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<UpdateUserResponse> MyWithPhoto(
            Principal principal,
            @RequestParam(required = false) MultipartFile photo,
            @RequestParam(required = false) Integer removePhoto,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password) throws IOException, LoadImageExceprion {

        Profile profile = Profile.builder()
                .name(name)
                .removePhoto(removePhoto)
                .password(password)
                .email(email)
                .photo(imageService.loadImage(photo))
                .build();

        return ResponseEntity.ok(userService.updateUser(principal.getName(), profile));
    }
}
