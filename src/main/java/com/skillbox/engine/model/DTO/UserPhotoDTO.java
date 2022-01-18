package com.skillbox.engine.model.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserPhotoDTO {
    private int id;
    private String name;
    private String photo;
}
