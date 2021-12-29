package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.CalendarResponse;
import com.skillbox.engine.api.response.PostResponse;
import com.skillbox.engine.api.response.TagResponse;
import com.skillbox.engine.model.DTO.PostDTO;
import com.skillbox.engine.service.PostsService;
import com.skillbox.engine.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiPostController {
    private final PostsService postsService;
    private final TagsService tagsService;


    @GetMapping("/post")
    public ResponseEntity<PostResponse> getPosts(@RequestParam(defaultValue = "0", value = "offset") int offSet,
                                                 @RequestParam(defaultValue = "10", value = "limit") int limit,
                                                 @RequestParam(defaultValue = "recent", value = "mode") String mode) {
        PostResponse postResponse = postsService.getAllPosts(offSet, limit, mode);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> getTag() {
        TagResponse tagResponse = tagsService.getAllTags();
        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping("/post/search")
    public ResponseEntity<PostResponse> getSearchPosts(@RequestParam(defaultValue = "0", value = "offset") int offSet,
                                                       @RequestParam(defaultValue = "10", value = "limit") int limit,
                                                       @RequestParam(defaultValue = "", value = "query") String query) {
        PostResponse postResponse = postsService.getSearchPost(offSet, limit, query);
        return ResponseEntity.ok(postResponse);
    }


    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam(defaultValue = "0", value = "year") int year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        CalendarResponse calendarResponse = postsService.getCalendar(year);
        return ResponseEntity.ok(calendarResponse);
    }

    @GetMapping("/post/byDate")
    public ResponseEntity<PostResponse> getAllPostsOnTheDate(@RequestParam(defaultValue = "0", value = "offset") int offSet,
                                                 @RequestParam(defaultValue = "10", value = "limit") int limit,
                                                 @RequestParam( value = "date") String date) {
        PostResponse postResponse = postsService.getAllPostsOnTheDate(offSet, limit, date);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/post/byTag")
    public ResponseEntity<PostResponse> getAllPostsByTag(@RequestParam(defaultValue = "0", value = "offset") int offSet,
                                                             @RequestParam(defaultValue = "10", value = "limit") int limit,
                                                             @RequestParam( value = "tag") String tag) {
        PostResponse postResponse = postsService.getPostsByTag(offSet, limit, tag);
        return ResponseEntity.ok(postResponse);
    }

}
