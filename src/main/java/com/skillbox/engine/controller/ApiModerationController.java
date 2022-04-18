package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.ModerationRequest;
import com.skillbox.engine.api.response.ModerationResponse;
import com.skillbox.engine.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class ApiModerationController {

    private final PostService postService;

    @PostMapping("/api/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<ModerationResponse> moderation(Principal principal, @RequestBody ModerationRequest moderationRequest) {

        ModerationResponse moderationResponse = postService.editModeration(principal.getName(), moderationRequest);

        return ResponseEntity.ok(moderationResponse);
    }
}
