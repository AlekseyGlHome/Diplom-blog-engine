package com.skillbox.engine.api.response;

import com.skillbox.engine.model.DTO.ErrorComment;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentErrorResponse {
    Boolean result;
    ErrorComment errors;
}
