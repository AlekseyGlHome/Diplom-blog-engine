package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.api.response.TagResponse;
import com.skillbox.engine.model.DTO.PostDTO;
import com.skillbox.engine.service.PostsService;
import com.skillbox.engine.service.TagsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiPostController {
    private final PostsService postsService;
    private final TagsService tagsService;

    public ApiPostController(PostsService postsService, TagsService tagsService) {
        this.postsService = postsService;
        this.tagsService = tagsService;
    }


    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPosts(@RequestParam(defaultValue = "0", value = "offset") int offSet,
                                   @RequestParam(defaultValue = "10", value = "limit") int limit,
                                   @RequestParam(defaultValue = "recent", value = "mode") String mode) {

        PostResponse postResponse = postsService.getAllPosts(offSet,limit,mode);

        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> getTag() {
        TagResponse tagResponse = tagsService.getAllTags();

        return ResponseEntity.ok(tagResponse);
    }

}
