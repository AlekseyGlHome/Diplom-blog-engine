package com.skillbox.engine.controller;

import com.skillbox.engine.api.response.CaptchaResponse;
import com.skillbox.engine.api.response.CheckResponse;
import com.skillbox.engine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<CheckResponse> check() {
        return ResponseEntity.ok(new CheckResponse());
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        CaptchaResponse captchaResponse = null;
        try {
            captchaResponse = authService.getCaptcha();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(captchaResponse);
    }

}
