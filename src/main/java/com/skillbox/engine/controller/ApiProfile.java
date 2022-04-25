package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ApiProfile {


    @PostMapping(value = "/my", consumes = "application/json")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public String MyWithoutPhoto(
            @RequestBody(required = false) Profile profile) {

        return "";
    }

    @PostMapping(value = "/my", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public String MyWithPhoto(@RequestParam(required = false) MultipartFile photo,
                              @RequestParam(required = false) Integer removePhoto,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String password) {

        return "";
    }
}
