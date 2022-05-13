package com.skillbox.engine.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class Profile {
    private String name;
    private String email;
    private String password;
    private Integer removePhoto;
    private String photo;
}
