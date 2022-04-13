package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.CommentRequest;
import com.skillbox.engine.api.response.CommentResponse;
import com.skillbox.engine.exception.CommentException;
import com.skillbox.engine.exception.NotFoundException;
import com.skillbox.engine.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {

    private final PostCommentService postCommentService;

    @PostMapping()
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<CommentResponse> commentPost(Principal principal, @RequestBody CommentRequest comment) throws NotFoundException, CommentException {

        return ResponseEntity.ok(postCommentService.addPostComment(principal.getName(), comment));
    }

}
