package com.skillbox.engine.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginRespons {
    private int id;
    private String name;
    private String photo;
    private String email;
    private boolean moderation;
    private long moderationCount;
    private boolean settings;
}
