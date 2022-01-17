package com.skillbox.engine.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import com.skillbox.engine.api.response.CaptchaResponse;
import com.skillbox.engine.model.entity.CaptchaCode;
import com.skillbox.engine.repository.CaptchaRepository;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CaptchaRepository captchaRepository;
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
