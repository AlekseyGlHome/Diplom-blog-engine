package com.skillbox.engine.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.skillbox.engine.api.request.UserRequest;
import com.skillbox.engine.api.response.CaptchaResponse;
import com.skillbox.engine.api.response.UserErrorRegister;
import com.skillbox.engine.api.response.UserRegisterResponse;
import com.skillbox.engine.model.entity.CaptchaCode;
import com.skillbox.engine.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
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
    private final Random random = new Random();

    @Value("${config.Captcha.StorageTime}")
    private int captchaStorageTime;

    @Value("${config.Captcha.Height}")
    private int captchaHeight;

    @Value("${config.Captcha.Width}")
    private int captchaWidth;

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

    public UserRegisterResponse checkingUserRegistration(UserRequest userRequest){
        deleteOldCaptcha();
        return errorChecking(userRequest);
    }

    private UserRegisterResponse errorChecking(UserRequest userRequest){
        UserErrorRegister userErrorRegister = new UserErrorRegister();
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        int error=0;
        if (userService.findEmail(userRequest.getEmail())>0){
            userErrorRegister.setEmail("Этот e-mail уже зарегистрирован");
            error++;
        }
        Optional<CaptchaCode> captchaCode = captchaRepository.findBySecretCodeEquals(userRequest.getCaptchaSecret());
        if (captchaCode.isEmpty()){
            userErrorRegister.setCaptcha("Код просрочен");
            error++;
        }else{
            if (!captchaCode.get().getCode().equals(userRequest.getCaptcha())){
                userErrorRegister.setCaptcha("Код с картинки введён неверно");
                error++;
            }
        }
        if (userRequest.getName().isEmpty() || userRequest.getName().isBlank()){
            userErrorRegister.setName("Имя указано неверно");
            error++;
        }
        if (userRequest.getPassword().length()<6){
            userErrorRegister.setPassword("Пароль короче 6-ти символов");
            error++;
        }
        if (error>0){
            userRegisterResponse.setResult(false);
        }else {
            userRegisterResponse.setResult(true);
            userService.addUser(userRequest);
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
        image +=Base64.getEncoder().encodeToString(resizeCaptcha(bufferedImage,gCage.getFormat()).toByteArray());
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
