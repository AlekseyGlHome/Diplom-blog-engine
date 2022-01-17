package com.skillbox.engine.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skillbox.engine.model.DTO.CommentDTO;
import com.skillbox.engine.model.DTO.PostDTO;
import com.skillbox.engine.model.DTO.UserDTO;
import lombok.*;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponse {
    private int id;
    @JsonProperty("timestamp")
    private Long time;
    private boolean active;
    private UserDTO user;
    private String title;
    private String text;
    private int likeCount;
    private int dislikeCount;
    private int viewCount;
    private Collection<CommentDTO> comments;
    private Collection<String> tags;
}
