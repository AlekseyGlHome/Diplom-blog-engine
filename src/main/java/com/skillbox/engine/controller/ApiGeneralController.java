package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.InitResponse;
import com.skillbox.engine.api.response.SettingsResponse;
import com.skillbox.engine.service.SettintgsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {
    private final InitResponse initResponse;
    private final SettintgsService settintgService;

    public ApiGeneralController(InitResponse initResponse,
                                SettintgsService settintgService) {
        this.initResponse = initResponse;
        this.settintgService = settintgService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<InitResponse> init() {
        return ResponseEntity.ok(initResponse);
    }

    @GetMapping("/api/settings")
    public ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(settintgService.getGlobalSettings());
    }

}
