package com.skillbox.engine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeDislikePostRequest {
    @JsonProperty("post_id")
    private Integer postId;
}
