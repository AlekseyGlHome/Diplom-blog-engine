package com.skillbox.engine.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.engine.model.DTO.ErrorUpdatePost;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PostUpdateResponse {
    private boolean result;
    private ErrorUpdatePost errors;
}
