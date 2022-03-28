package com.skillbox.engine.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class PostRequest {
    private Long timestamp;
    private byte active;
    private String title;
    private String[] tags;
    private String text;
}
