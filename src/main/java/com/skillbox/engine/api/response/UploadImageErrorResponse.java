package com.skillbox.engine.api.response;

import com.skillbox.engine.model.DTO.ErrorUploadImage;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadImageErrorResponse {
    Boolean result;
    ErrorUploadImage errors;
}
