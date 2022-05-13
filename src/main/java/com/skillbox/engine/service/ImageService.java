package com.skillbox.engine.service;

import com.skillbox.engine.exception.LoadImageExceprion;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {
    @Value("${config.endOfFilePath}")
    private String uploadFile;

    public String loadImage(MultipartFile file) throws IOException, LoadImageExceprion {
        if (file.isEmpty()) {
            return "";
        }
        if (!Objects.requireNonNull(file.getContentType()).contains("image")){
            throw new LoadImageExceprion("Отправлен файл не формата изображение jpg, png.");
        }
        if (file.getSize()>=1048576){
            throw new LoadImageExceprion("Размер файла превышает допустимый размер");
        }
        String randomPath = randomPathGeneration();
        File pathDir = new File(uploadFile + "/" + randomPath);
        if (!pathDir.exists()) {
            pathDir.mkdirs();
        }

        String uuidNewNameFile = UUID.randomUUID().toString();
        String resultFileName = uuidNewNameFile + "." + file.getOriginalFilename();
        file.transferTo(new File(pathDir + "/" + resultFileName));

        return randomPath + resultFileName;
    }

    public ByteArrayOutputStream resizeImage(BufferedImage image, String format, int width, int height) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BufferedImage newImage = Scalr.resize(image, Scalr.Method.QUALITY, Scalr.Mode.AUTOMATIC, width, height);
        ImageIO.write(newImage, format, outputStream);
        return outputStream;
    }

    private String randomPathGeneration() {
        StringBuilder path = new StringBuilder();
        //int countPath = ((int) (Math.random() * 3)) + 1;
        for (int i = 0; i < 3; i++) {
            path.append(oneGeneratePath()).append("/");
        }
        return path.toString();
    }

    private String oneGeneratePath() {
        char[] alphabet = new char[]{'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
        StringBuilder pathTemp = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int index = ((int) (Math.random() * 26));
            pathTemp.append(alphabet[index]);
        }
        return pathTemp.toString();
    }
}
