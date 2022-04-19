package com.skillbox.engine.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.skillbox.engine.api.request.UserLoginRequest;
import com.skillbox.engine.api.request.UserRegistrRequest;
import com.skillbox.engine.api.response.*;
import com.skillbox.engine.model.entity.CaptchaCode;
import com.skillbox.engine.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CaptchaRepository captchaRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PostService postService;

    private final Random random = new Random();

    @Value("${config.Captcha.StorageTime}")
    private int captchaStorageTime;

    @Value("${config.Captcha.Height}")
    private int captchaHeight;

    @Value("${config.Captcha.Width}")
    private int captchaWidth;

    public LoginRespons logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        LoginRespons loginRespons = new LoginRespons();
        loginRespons.setResult(true);
        return loginRespons;
    }

    public LoginRespons getAuthentication(UserLoginRequest userLoginRequest) {
        User user;
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(),
                            userLoginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            user = (User) auth.getPrincipal();
        }catch (BadCredentialsException ex){
            return new LoginRespons();
        }

        return getLoginRespons(user.getUsername());
    }

    public LoginRespons getLoginRespons(String email) {
        com.skillbox.engine.model.entity.User userEntity = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user " + email + " not found"));
        LoginRespons loginRespons = new LoginRespons();
        loginRespons.setResult(true);
        UserLoginRespons userLoginRespons = buildUserLoginRespons(userEntity);

        loginRespons.setUserLoginRespons(userLoginRespons);
        return loginRespons;
    }

    private UserLoginRespons buildUserLoginRespons(com.skillbox.engine.model.entity.User userEntity) {
        return UserLoginRespons.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .photo(userEntity.getPhoto())
                .email(userEntity.getEmail())
                .moderation(userEntity.getModeration())
                .moderationCount(getModerationCount(userEntity.getModeration()))
                .settings(userEntity.getModeration())
                .build();
    }

    private long getModerationCount(boolean isModeration){
        if (isModeration){
            return postService.numberOfPostsForModeration();
        }
        return 0;
    }

    public CaptchaResponse getCaptcha() throws IOException {
        String secretCode = UUID.randomUUID().toString().replaceAll("-", "");
        String captchaText = generateCaptchaText();
        String image = generateCaptchaImage(captchaText);
        CaptchaCode captchaCode = new CaptchaCode();
        captchaCode.setCode(captchaText);
        captchaCode.setSecretCode(secretCode);
        captchaCode.setTime(LocalDateTime.now());
        captchaRepository.save(captchaCode);
        deleteOldCaptcha();
        return new CaptchaResponse(secretCode, image);
    }

    public UserRegisterResponse checkingUserRegistration(UserRegistrRequest userRegistrRequest) {
        deleteOldCaptcha();
        return errorChecking(userRegistrRequest);
    }

    private UserRegisterResponse errorChecking(UserRegistrRequest userRegistrRequest) {
        UserErrorRegister userErrorRegister = new UserErrorRegister();
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        int error = 0;
        if (userService.findCountEmail(userRegistrRequest.getEmail()) > 0) {
            userErrorRegister.setEmail("Этот e-mail уже зарегистрирован");
            error++;
        }
        Optional<CaptchaCode> captchaCode = captchaRepository.findBySecretCodeEquals(userRegistrRequest.getCaptchaSecret());
        if (captchaCode.isEmpty()) {
            userErrorRegister.setCaptcha("Код просрочен");
            error++;
        } else {
            if (!captchaCode.get().getCode().equalsIgnoreCase(userRegistrRequest.getCaptcha())) {
                userErrorRegister.setCaptcha("Код с картинки введён неверно");
                error++;
            }
        }
        if (userRegistrRequest.getName().isEmpty() || userRegistrRequest.getName().isBlank()) {
            userErrorRegister.setName("Имя указано неверно");
            error++;
        }
        if (userRegistrRequest.getPassword().length() < 6) {
            userErrorRegister.setPassword("Пароль короче 6-ти символов");
            error++;
        }
        if (error > 0) {
            userRegisterResponse.setResult(false);
        } else {
            userRegisterResponse.setResult(true);
            userService.addUser(userRegistrRequest);
        }
        userRegisterResponse.setErrors(userErrorRegister);
        return userRegisterResponse;
    }

    private void deleteOldCaptcha() {
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(captchaStorageTime);
        captchaRepository.deleteByTimeBefore(localDateTime);
    }

    private String generateCaptchaImage(String captchaText) throws IOException {
        Cage gCage = new GCage();
        BufferedImage bufferedImage = gCage.drawImage(captchaText);
        String image = "data:image/".concat(gCage.getFormat()).concat(";base64,");
        image += Base64.getEncoder().encodeToString(resizeCaptcha(bufferedImage, gCage.getFormat()).toByteArray());
        return image;
    }

    private ByteArrayOutputStream resizeCaptcha(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage newImage = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, captchaWidth, captchaHeight);
        ImageIO.write(newImage, format, outputStream);
        return outputStream;
    }

    private String generateCaptchaText() {
        char[] chars = "ACEFGHJKLMNPQRUVWXY1234567890".toCharArray();
        var stringBuilder = new StringBuilder();
        for (var i = 0; i < 5; i++) {
            stringBuilder.append(chars[random.nextInt(chars.length)]);
        }
        return stringBuilder.toString();
    }
}
