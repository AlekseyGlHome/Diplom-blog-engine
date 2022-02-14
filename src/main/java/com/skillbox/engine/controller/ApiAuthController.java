package com.skillbox.engine.controller;

import com.skillbox.engine.api.request.UserLoginRequest;
import com.skillbox.engine.api.request.UserRegistrRequest;
import com.skillbox.engine.api.response.CaptchaResponse;
import com.skillbox.engine.api.response.LoginRespons;
import com.skillbox.engine.api.response.UserRegisterResponse;
import com.skillbox.engine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class ApiAuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<LoginRespons> check(Principal principal) {
        if (principal == null) {
            return ResponseEntity.ok(new LoginRespons());
        }
        return ResponseEntity.ok(authService.getLoginRespons(principal.getName()));
    }

    @GetMapping("/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        CaptchaResponse captchaResponse = null;
        try {
            captchaResponse = authService.getCaptcha();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert captchaResponse != null;
        return ResponseEntity.ok(captchaResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody UserRegistrRequest userRegistrRequest) {
        return ResponseEntity.ok(authService.checkingUserRegistration(userRegistrRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginRespons> login(@RequestBody UserLoginRequest userLoginRequest) {

        return ResponseEntity.ok(authService.getAuthentication(userLoginRequest));
    }

    @GetMapping("/logout")
    public ResponseEntity<LoginRespons> logout(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(authService.logout(request, response));
    }


}
