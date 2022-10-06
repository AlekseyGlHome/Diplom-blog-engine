package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.LikeDislikePostRequest;
import com.skillbox.engine.api.request.PostRequest;
import com.skillbox.engine.api.response.LikeDislikePostResponse;
import com.skillbox.engine.api.response.PostDetailResponse;
import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.api.response.PostUpdateResponse;
import com.skillbox.engine.exception.NotFoundException;
import com.skillbox.engine.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/post")
public class ApiPostController {
    private final PostService postsService;

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
    public ResponseEntity<PostDetailResponse> getPostById(Principal principal, @PathVariable int id)
            throws NotFoundException {
        String userEmail = null;
        if (principal != null) {
            userEmail = principal.getName();
        }
        return ResponseEntity.ok(postsService.getPostById(id, userEmail));
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
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<PostUpdateResponse> addPost(Principal principal, @RequestBody PostRequest postRequest)
            throws NotFoundException {
        PostUpdateResponse postUpdateResponse = postsService.addPost(principal.getName(), postRequest);
        return ResponseEntity.ok(postUpdateResponse);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<PostUpdateResponse> editPost(Principal principal, @PathVariable int id,
                                                       @RequestBody PostRequest postRequest)
            throws NotFoundException {
        PostUpdateResponse postUpdateResponse = postsService.editPost(principal.getName(), postRequest, id);
        return ResponseEntity.ok(postUpdateResponse);
    }

    @PostMapping("/like")
    @PreAuthorize("hasAnyAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<LikeDislikePostResponse> like(Principal principal,
                                                        @RequestBody LikeDislikePostRequest likeDislikePostRequest)
            throws NotFoundException {
        LikeDislikePostResponse response = postsService.addLike(likeDislikePostRequest.getPostId(), principal.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/dislike")
    @PreAuthorize("hasAnyAuthority('user:moderate')||hasAuthority('user:write')")
    public ResponseEntity<LikeDislikePostResponse> dislake(Principal principal,
                                                           @RequestBody LikeDislikePostRequest likeDislikePostRequest)
            throws NotFoundException {
        LikeDislikePostResponse response = postsService.addDislike(likeDislikePostRequest.getPostId(), principal.getName());
        return ResponseEntity.ok(response);
    }
}
