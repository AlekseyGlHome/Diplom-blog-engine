package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.PostDetailResponse;
import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.exception.NotFoundException;
import com.skillbox.engine.model.enums.PostModerationStatus;
import com.skillbox.engine.service.PostService;
import com.skillbox.engine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    private final PostService postsService;
    private final TagService tagsService;


    @GetMapping()
    public ResponseEntity<PostResponse> getPosts(
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(defaultValue = "recent", value = "mode") String mode) {
        PostResponse postResponse = postsService.getAllPosts(offSet, limit, mode);
        return ResponseEntity.ok(postResponse);
    }


    @GetMapping("/search")
    public ResponseEntity<PostResponse> getSearchPosts(
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(defaultValue = "", value = "query") String query) {
        PostResponse postResponse = postsService.getSearchPost(offSet, limit, query);
        return ResponseEntity.ok(postResponse);
    }


    @GetMapping("/byDate")
    public ResponseEntity<PostResponse> getAllPostsOnTheDate(
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(value = "date") String date) {
        PostResponse postResponse = postsService.getAllPostsOnTheDate(offSet, limit, date);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/byTag")
    public ResponseEntity<PostResponse> getAllPostsByTag(
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(value = "tag") String tag) {
        PostResponse postResponse = postsService.getPostsByTag(offSet, limit, tag);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailResponse> getPostById(@PathVariable int id) throws NotFoundException {
        return ResponseEntity.ok(postsService.getPostById(id));
    }

    @GetMapping("/my")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<PostResponse> getMyPost(
            Principal principal,
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(defaultValue = "inactive", value = "status") String status)
            throws NotFoundException {

        PostResponse postResponse = postsService.getMyPosts(offSet, limit, status, principal.getName());
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/moderation")
    @PreAuthorize("hasAuthority('user:moderate')")
    public ResponseEntity<PostResponse> getPostModeration(
            Principal principal,
            @RequestParam(defaultValue = "0", value = "offset") int offSet,
            @RequestParam(defaultValue = "10", value = "limit") int limit,
            @RequestParam(defaultValue = "new", value = "status") String status)
            throws NotFoundException {

        PostResponse postResponse = postsService.getPostsModeration(offSet, limit, status, principal.getName());
        return ResponseEntity.ok(postResponse);
    }

    @PostMapping()
    public ResponseEntity<String> addPost() {

        return ResponseEntity.ok("");
    }
}
