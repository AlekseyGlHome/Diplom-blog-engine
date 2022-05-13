package com.skillbox.engine.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.skillbox.engine.model.DTO.ErrorUserUpdate;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserResponse {
    boolean result;
    private ErrorUserUpdate errors;
}
