package com.skillbox.engine.model.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorUpdatePost {
    private String title;
    private String text;
}
