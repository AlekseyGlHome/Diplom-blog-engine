package com.skillbox.engine.api.response;

import com.skillbox.engine.model.DTO.PostDTO;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostResponse {
    private long count;
    private Collection<PostDTO> posts = new ArrayList<>();
}
