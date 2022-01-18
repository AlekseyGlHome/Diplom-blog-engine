package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.CalendarResponse;
import com.skillbox.engine.api.response.InitResponse;
import com.skillbox.engine.api.response.SettingsResponse;
import com.skillbox.engine.api.response.TagResponse;
import com.skillbox.engine.service.PostService;
import com.skillbox.engine.service.SettintgService;
import com.skillbox.engine.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettintgService settintgService;
    private final TagService tagsService;
    private final PostService postsService;

    @GetMapping("/init")
    public ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settintgService.getGlobalSettings());
    }

    @GetMapping("/tag")
    public ResponseEntity<TagResponse> getTag() {
        TagResponse tagResponse = tagsService.getAllTags();
        return ResponseEntity.ok(tagResponse);
    }

    @GetMapping("/calendar")
    public ResponseEntity<CalendarResponse> getCalendar(@RequestParam(defaultValue = "0", value = "year") int year) {
        if (year == 0) {
            year = LocalDate.now().getYear();
        }
        CalendarResponse calendarResponse = postsService.getCalendar(year);
        return ResponseEntity.ok(calendarResponse);
    }
}
