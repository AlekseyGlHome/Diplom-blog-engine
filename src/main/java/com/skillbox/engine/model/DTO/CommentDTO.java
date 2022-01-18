package com.skillbox.engine.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private int id;
    @JsonProperty("timestamp")
    private long time;
    private String text;
    private UserPhotoDTO user;


}
