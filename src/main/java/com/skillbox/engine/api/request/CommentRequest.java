package com.skillbox.engine.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @JsonProperty("parent_id")
    Integer parentId;
    @JsonProperty("post_id")
    Integer postId;
    String text;
}
